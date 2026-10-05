package com.dochiri.tooltestlab.global.domain;

import org.junit.jupiter.api.DisplayName;
import com.dochiri.tooltestlab.cart.domain.outbox.CartOutbox;
import com.dochiri.tooltestlab.catalog.product.domain.outbox.CatalogOutbox;
import com.dochiri.tooltestlab.global.domain.outbox.Outbox;
import com.dochiri.tooltestlab.member.domain.outbox.MemberOutbox;
import com.dochiri.tooltestlab.order.domain.outbox.OrderOutbox;
import com.dochiri.tooltestlab.review.domain.outbox.ReviewOutbox;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OutboxTest {
    private static final Instant NOW = Instant.parse("2026-10-04T00:00:00Z");
    private static final Outbox.Message MESSAGE = new Outbox.Message(
            "01900000-0000-7000-8000-000000000001", "event.v1",
            "01900000-0000-7000-8000-000000000002", 1, NOW, "{}");

    static Stream<Arguments> outboxes() {
        return Stream.of(
                Arguments.of("catalog", new CatalogOutbox(MESSAGE)),
                Arguments.of("cart", new CartOutbox(MESSAGE)),
                Arguments.of("order", new OrderOutbox(MESSAGE)),
                Arguments.of("member", new MemberOutbox(MESSAGE)),
                Arguments.of("review", new ReviewOutbox(MESSAGE)));
    }

    @ParameterizedTest
    @MethodSource("outboxes")
    @DisplayName("백오프와 발행을 거쳐도 소유 BC와 식별 정보를 유지한다")
    void retains_ownership_and_identity_through_backoff_and_publication(final String source, final Outbox outbox) {
        // given
        final IllegalStateException failure = new IllegalStateException("전송 실패");
        Outbox current = outbox;
        Instant now = NOW;
        int attempts = 0;

        // when & then
        assertEquals(source, outbox.source());
        assertTrue(outbox.ready(NOW));
        for (final int delay : new int[]{1, 2, 4, 8, 16, 32, 60, 60}) {
            final Outbox previous = current;
            final Outbox.Delivery previousDelivery = previous.delivery();
            current = previous.failed(now, failure);
            assertNotSame(previous, current);
            assertSame(previousDelivery, previous.delivery());
            assertEquals(++attempts, current.delivery().attempts());
            assertEquals(now.plusSeconds(delay), current.delivery().nextAttemptAt());
            assertFalse(current.ready(now.plusSeconds(delay).minusNanos(1)));
            now = now.plusSeconds(delay);
            assertTrue(current.ready(now));
            assertSame(MESSAGE, current.message());
            assertEquals(source, current.source());
            assertEquals(outbox.getClass(), current.getClass());
        }
        final Outbox published = current.published(now);
        assertNotSame(current, published);
        assertNull(current.delivery().publishedAt());
        assertNotNull(current.delivery().lastError());
        assertTrue(current.ready(now));
        assertEquals(now, published.delivery().publishedAt());
        assertEquals(current.delivery().nextAttemptAt(), published.delivery().nextAttemptAt());
        assertEquals(current.delivery().attempts(), published.delivery().attempts());
        assertNull(published.delivery().lastError());
        assertFalse(published.ready(now.plusSeconds(1000)));
        assertSame(MESSAGE, published.message());
        assertEquals(source, published.source());
        assertEquals(outbox.getClass(), published.getClass());
        assertEquals(new Outbox.Delivery(null, NOW, 0, null), outbox.delivery());
        assertTrue(outbox.ready(NOW));
    }

    @ParameterizedTest
    @MethodSource("outboxes")
    @DisplayName("저장하는 오류 길이를 제한하되 실패 유형은 유지한다")
    void bounds_persisted_error_without_losing_the_failure_type(final String source, final Outbox outbox) {
        // given
        final Outbox failed = outbox.failed(NOW, new IllegalStateException("x".repeat(2000)));

        // when & then
        assertEquals(1000, failed.delivery().lastError().length());
        assertTrue(failed.delivery().lastError().startsWith("IllegalStateException: "));
    }
}
