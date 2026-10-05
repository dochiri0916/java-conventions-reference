package com.dochiri.tooltestlab.messaging;

import com.dochiri.tooltestlab.ToolTestLabApplication;
import com.dochiri.tooltestlab.global.messaging.MessageEnvelope;
import com.dochiri.tooltestlab.global.messaging.persistence.InboxProcessor;
import com.dochiri.tooltestlab.member.application.port.in.command.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.usecase.RegisterMemberUseCase;
import com.dochiri.tooltestlab.order.adapter.out.persistence.inbox.OrderInboxEntry;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest(classes = ToolTestLabApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
        "messaging.outbox.enabled=false",
        "messaging.snapshots.publish-on-startup=false",
        "spring.datasource.url=jdbc:h2:mem:inbox-processor;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "logging.level.root=WARN",
        "logging.level.org.hibernate.SQL=OFF"
})
class InboxProcessorTest {
    @Autowired InboxProcessor inbox;
    @Autowired RegisterMemberUseCase registerMember;
    @Autowired JdbcTemplate jdbc;
    @Autowired EntityManager entityManager;
    @Autowired PlatformTransactionManager transactionManager;

    MessageEnvelope message;
    Runnable handler;
    AtomicInteger handled;

    @BeforeEach
    void reset() {
        jdbc.update("delete from order_inbox");
        jdbc.update("delete from member_outbox");
        jdbc.update("delete from members");
        final String eventId = UuidCreator.getTimeOrderedEpoch().toString();
        message = new MessageEnvelope(eventId, "test", "test.v1", eventId, 1, Instant.now(), "{}");
        handled = new AtomicInteger();
        handler = () -> {
            handled.incrementAndGet();
            registerMember.register(new RegisterMemberCommand(eventId + "@test.com", "1234", "회원"));
        };
    }

    @Test
    void failedHandlerRollsBackInboxBusinessChangeAndFollowUpOutboxAndAllowsRetry() {
        assertThrows(IllegalStateException.class, () -> inbox.process(message, OrderInboxEntry.class,
                OrderInboxEntry::new, () -> {
                    handler.run();
                    throw new IllegalStateException("처리 실패");
                }));
        assertCounts(0);

        process();
        process();
        assertCounts(1);
        assertEquals(2, handled.get()); // 롤백된 시도와 성공한 재시도만 실행된다.
        new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            final var restored = entityManager.find(OrderInboxEntry.class, message.eventId());
            assertNotNull(restored);
            assertEquals(message.eventId(), entityManager.getEntityManagerFactory().getPersistenceUnitUtil()
                    .getIdentifier(restored));
        });
    }

    @Test
    void concurrentDuplicateIsBlockedBeforeHandlerAndSkipsOnRedelivery() throws Exception {
        final var bothMissing = new CyclicBarrier(2);
        final InboxProcessor.EntryFactory factory = (eventId, eventType, processedAt) -> {
            try {
                bothMissing.await(5, TimeUnit.SECONDS);
            } catch (final Exception failure) {
                throw new IllegalStateException("동시 수신 준비 실패", failure);
            }
            return new OrderInboxEntry(eventId, eventType, processedAt);
        };
        final Callable<Boolean> receive = () -> {
            try {
                inbox.process(message, OrderInboxEntry.class, factory, handler);
                return true;
            } catch (final RuntimeException retryable) {
                return false;
            }
        };
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final var results = executor.invokeAll(List.of(receive, receive), 10, TimeUnit.SECONDS);
            int committed = 0;
            for (final var result : results) {
                if (result.get()) {
                    committed++;
                }
            }
            assertEquals(1, committed);
        }
        assertCounts(1);
        assertEquals(1, handled.get());
        process();
        assertCounts(1);
        assertEquals(1, handled.get());
    }

    private void process() {
        inbox.process(message, OrderInboxEntry.class, OrderInboxEntry::new, handler);
    }

    private void assertCounts(final long expected) {
        for (final var table : List.of("order_inbox", "members", "member_outbox")) {
            assertEquals(expected, jdbc.queryForObject("select count(*) from " + table, Long.class), table);
        }
    }
}
