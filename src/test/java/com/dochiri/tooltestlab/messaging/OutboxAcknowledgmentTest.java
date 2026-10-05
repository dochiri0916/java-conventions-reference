package com.dochiri.tooltestlab.messaging;

import com.dochiri.tooltestlab.ToolTestLabApplication;
import com.dochiri.tooltestlab.catalog.product.adapter.out.persistence.outbox.CatalogOutboxEntry;
import com.dochiri.tooltestlab.catalog.product.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.catalog.product.application.port.out.event.ProductChangedIntegrationEvent;
import com.dochiri.tooltestlab.global.messaging.LocalMessageBroker;
import com.dochiri.tooltestlab.global.messaging.LocalMessageReceiver;
import com.dochiri.tooltestlab.global.messaging.MessageBroker;
import com.dochiri.tooltestlab.global.messaging.MessageEnvelope;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxDispatcher;
import com.github.f4b6a3.uuid.UuidCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = {ToolTestLabApplication.class, OutboxAcknowledgmentTest.Config.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
        "messaging.transport=test",
        "messaging.outbox.enabled=false",
        "messaging.outbox.send-timeout-ms=3000",
        "messaging.snapshots.publish-on-startup=false",
        "spring.datasource.url=jdbc:h2:mem:outbox-ack;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "logging.level.root=WARN",
        "logging.level.org.hibernate.SQL=OFF"
})
class OutboxAcknowledgmentTest {
    @Autowired IntegrationEventPort events;
    @Autowired OutboxDispatcher dispatcher;
    @Autowired DeferredBroker broker;
    @Autowired JdbcTemplate jdbc;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired ApplicationContext context;

    @BeforeEach
    void append() {
        jdbc.update("delete from catalog_outbox");
        broker.ack = new CompletableFuture<>();
        broker.accepted = new CountDownLatch(1);
        broker.message = null;
        new TransactionTemplate(transactionManager).executeWithoutResult(status -> events.publish(
                new ProductChangedIntegrationEvent(UuidCreator.getTimeOrderedEpoch().toString(), "상품", 100, 10, "ACTIVE")));
    }

    @Test
    void publishesOnlyAfterTransportAcknowledgmentWithoutWaitingForConsumers() throws Exception {
        assertTrue(context.getBeansOfType(LocalMessageBroker.class).isEmpty());
        assertTrue(context.getBeansOfType(LocalMessageReceiver.class).isEmpty());
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final var result = executor.submit(() -> dispatcher.dispatch(CatalogOutboxEntry.class));
            try {
                assertTrue(broker.accepted.await(2, TimeUnit.SECONDS));
                assertFalse(result.isDone());
                assertEquals(1L, pending());
                assertEquals("catalog", broker.message.source());
                assertEquals(jdbc.queryForObject("select id from catalog_outbox", Long.class), broker.message.sequence());
            } finally {
                broker.ack.complete(null);
            }
            assertEquals(1, result.get(5, TimeUnit.SECONDS));
        }
        assertEquals(0L, pending());
        assertEquals(0L, jdbc.queryForObject("select count(*) from cart_inbox", Long.class));
    }

    @Test
    void failedAcknowledgmentPersistsRetryAndOriginalCause() {
        broker.ack.completeExceptionally(new IllegalStateException("전송 거절"));
        assertEquals(0, dispatcher.dispatch(CatalogOutboxEntry.class));
        assertEquals(1L, pending());
        assertEquals(1, jdbc.queryForObject("select attempts from catalog_outbox", Integer.class));
        assertEquals("IllegalStateException: 전송 거절", jdbc.queryForObject("select last_error from catalog_outbox", String.class));
        assertEquals(broker.message.eventId(), jdbc.queryForObject("select event_id from catalog_outbox", String.class));
    }

    @Test
    void missingAcknowledgmentTimesOutAndLeavesTheEventPending() {
        assertEquals(0, dispatcher.dispatch(CatalogOutboxEntry.class));
        assertEquals(1L, pending());
        assertEquals(1, jdbc.queryForObject("select attempts from catalog_outbox", Integer.class));
        assertTrue(jdbc.queryForObject("select last_error from catalog_outbox", String.class).startsWith("TimeoutException:"));
    }

    private long pending() {
        return jdbc.queryForObject("select count(*) from catalog_outbox where published_at is null", Long.class);
    }

    @TestConfiguration
    static class Config {
        @Bean
        DeferredBroker deferredBroker() {
            return new DeferredBroker();
        }

        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(Instant.parse("2026-10-04T00:00:00Z"), ZoneOffset.UTC);
        }
    }

    static final class DeferredBroker implements MessageBroker {
        CompletableFuture<Void> ack;
        CountDownLatch accepted;
        MessageEnvelope message;

        @Override
        public CompletionStage<Void> send(final MessageEnvelope message) {
            this.message = message;
            accepted.countDown();
            return ack;
        }
    }
}
