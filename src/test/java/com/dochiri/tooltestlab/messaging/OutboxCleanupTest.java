package com.dochiri.tooltestlab.messaging;

import org.junit.jupiter.api.DisplayName;
import com.dochiri.tooltestlab.ToolTestLabApplication;
import com.dochiri.tooltestlab.cart.adapter.out.persistence.outbox.CartOutboxEntry;
import com.dochiri.tooltestlab.cart.domain.outbox.CartOutbox;
import com.dochiri.tooltestlab.catalog.product.adapter.out.persistence.outbox.CatalogOutboxEntry;
import com.dochiri.tooltestlab.catalog.product.domain.outbox.CatalogOutbox;
import com.dochiri.tooltestlab.global.domain.outbox.Outbox;
import com.dochiri.tooltestlab.global.configuration.properties.OutboxProperties;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxCleanup;
import com.dochiri.tooltestlab.global.messaging.persistence.OutboxEntry;
import com.dochiri.tooltestlab.global.messaging.persistence.RequiresNewTransaction;
import com.dochiri.tooltestlab.member.adapter.out.persistence.outbox.MemberOutboxEntry;
import com.dochiri.tooltestlab.member.domain.outbox.MemberOutbox;
import com.dochiri.tooltestlab.order.adapter.out.persistence.outbox.OrderOutboxEntry;
import com.dochiri.tooltestlab.order.domain.outbox.OrderOutbox;
import com.dochiri.tooltestlab.review.adapter.out.persistence.outbox.ReviewOutboxEntry;
import com.dochiri.tooltestlab.review.domain.outbox.ReviewOutbox;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(classes = {ToolTestLabApplication.class, OutboxCleanupTest.Config.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
        "messaging.outbox.enabled=false",
        "messaging.outbox.cleanup.retention-days=7",
        "messaging.outbox.cleanup.batch-size=2",
        "messaging.outbox.cleanup.max-batches=2",
        "messaging.snapshots.publish-on-startup=false",
        "spring.datasource.url=jdbc:h2:mem:outbox-cleanup;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "logging.level.root=WARN",
        "logging.level.org.hibernate.SQL=OFF"
})
class OutboxCleanupTest {
    private static final Instant NOW = Instant.parse("2026-10-04T00:00:00Z");

    @Autowired OutboxCleanup cleanup;
    @Autowired EntityManager entityManager;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired JdbcTemplate jdbc;
    @Autowired Clock clock;

    static Stream<Queue> queues() {
        return Stream.of(
                new Queue("catalog_outbox", message -> new CatalogOutboxEntry(new CatalogOutbox(message))),
                new Queue("cart_outbox", message -> new CartOutboxEntry(new CartOutbox(message))),
                new Queue("order_outbox", message -> new OrderOutboxEntry(new OrderOutbox(message))),
                new Queue("member_outbox", message -> new MemberOutboxEntry(new MemberOutbox(message))),
                new Queue("review_outbox", message -> new ReviewOutboxEntry(new ReviewOutbox(message))));
    }

    @BeforeEach
    void reset() {
        queues().forEach(queue -> jdbc.update("delete from " + queue.table()));
        jdbc.update("delete from order_inbox");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("queues")
    @DisplayName("보관 기간이 지난 완료 이벤트만 삭제하고 Inbox는 유지한다")
    void deletes_only_expired_completed_events_and_keeps_inbox(final Queue queue) {
        // given
        final Instant cutoff = NOW.minus(Duration.ofDays(7));
        final String expired = append(queue, cutoff.minusSeconds(1), false);
        final String boundary = append(queue, cutoff, false);
        final String recent = append(queue, NOW.minusSeconds(1), false);
        final String pending = append(queue, null, false);
        final String failed = append(queue, null, true);
        jdbc.update("insert into order_inbox (event_id, event_type, processed_at) values (?, ?, ?)",
                expired, "test.v1", Timestamp.from(NOW.minus(Duration.ofDays(30))));
        cleanup.cleanup();

        // when & then
        assertEquals(0, count(queue, expired));
        assertEquals(1, count(queue, boundary));
        assertEquals(1, count(queue, recent));
        assertEquals(1, count(queue, pending));
        assertEquals(1, count(queue, failed));
        assertEquals(1L, jdbc.queryForObject("select count(*) from order_inbox", Long.class));
    }

    @Test
    @DisplayName("실행마다 배치를 제한하고 다음 실행에서 오래된 것부터 이어 삭제한다")
    void bounds_each_run_and_continues_oldest_first_on_the_next_run() {
        // given
        final Queue queue = queues().findFirst().orElseThrow();
        final var events = new ArrayList<String>();
        for (int i = 0; i < 7; i++) {
            events.add(append(queue, NOW.minus(Duration.ofDays(10)).plusSeconds(6 - i), false));
        }
        cleanup.cleanup();

        // when & then
        for (int i = 0; i < events.size(); i++) {
            assertEquals(i < 3 ? 1 : 0, count(queue, events.get(i)));
        }
        cleanup.cleanup();
        assertEquals(0L, jdbc.queryForObject("select count(*) from " + queue.table(), Long.class));
        assertDoesNotThrow(cleanup::cleanup);
    }

    @Test
    @DisplayName("보관 기간이 0이면 다음 정리에서 완료 이벤트를 삭제한다")
    void zero_retention_deletes_completed_events_on_the_next_cleanup() {
        // given
        final Queue queue = queues().findFirst().orElseThrow();
        final String completed = append(queue, NOW.minusSeconds(1), false);
        final String pending = append(queue, null, false);
        new OutboxCleanup(entityManager, clock, new RequiresNewTransaction(transactionManager),
                new OutboxProperties(10000, new OutboxProperties.Cleanup(0, 2, 2))).cleanup();

        // when & then
        assertEquals(0, count(queue, completed));
        assertEquals(1, count(queue, pending));
    }

    @Test
    @DisplayName("잘못된 보관 기간과 배치 한도를 거부한다")
    void rejects_invalid_retention_and_batch_limits() {
        // given
        final int validRetentionDays = 7;
        final int validBatchSize = 2;

        // when & then
        assertThrows(IllegalArgumentException.class,
                () -> new OutboxProperties.Cleanup(-1, validBatchSize, validBatchSize));
        assertThrows(IllegalArgumentException.class,
                () -> new OutboxProperties.Cleanup(validRetentionDays, 0, validBatchSize));
        assertThrows(IllegalArgumentException.class,
                () -> new OutboxProperties.Cleanup(validRetentionDays, validBatchSize, 0));
        assertThrows(IllegalArgumentException.class, () -> new OutboxProperties(0, new OutboxProperties.Cleanup(1, 1, 1)));
    }

    private String append(final Queue queue, final Instant publishedAt, final boolean failed) {
        final String eventId = UuidCreator.getTimeOrderedEpoch().toString();
        final var entity = queue.factory().apply(new Outbox.Message(eventId, "test.v1", eventId, 0,
                NOW.minus(Duration.ofDays(30)), "{}"));
        final var outbox = entity.toDomain();
        final var attempted = failed
                ? outbox.failed(NOW.minus(Duration.ofDays(30)), new IllegalStateException("전송 실패")) : outbox;
        entity.update(publishedAt == null ? attempted : attempted.published(publishedAt));
        new TransactionTemplate(transactionManager).executeWithoutResult(status -> entityManager.persist(entity));
        return eventId;
    }

    private int count(final Queue queue, final String eventId) {
        return jdbc.queryForObject("select count(*) from " + queue.table() + " where event_id = ?", Integer.class, eventId);
    }

    private record Queue(String table, Function<Outbox.Message, OutboxEntry> factory) {
        @Override
        public String toString() {
            return table;
        }
    }

    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        Clock fixedClock() {
            return Clock.fixed(NOW, ZoneOffset.UTC);
        }
    }
}
