package com.dochiri.tooltestlab.messaging;

import org.junit.jupiter.api.DisplayName;
import com.dochiri.tooltestlab.ToolTestLabApplication;
import com.dochiri.tooltestlab.cart.application.port.in.query.GetCartQuery;
import com.dochiri.tooltestlab.catalog.product.application.port.in.query.GetProductQuery;
import com.dochiri.tooltestlab.order.application.port.in.query.GetOrderQuery;
import com.dochiri.tooltestlab.cart.application.port.in.command.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.command.RemoveFromCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.AddToCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.GetCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.PublishCartSnapshotsUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.RemoveFromCartUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.usecase.GetProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.usecase.PublishProductSnapshotsUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.usecase.RegisterProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.out.event.ProductChangedIntegrationEvent;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.CategoryPublicIds;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.global.messaging.EventCodec;
import com.dochiri.tooltestlab.global.messaging.IntegrationEvent;
import com.dochiri.tooltestlab.global.messaging.MessageBroker;
import com.dochiri.tooltestlab.global.messaging.MessageEnvelope;
import com.dochiri.tooltestlab.global.messaging.OutboxRelay;
import com.dochiri.tooltestlab.member.application.port.in.command.RegisterMemberCommand;
import com.dochiri.tooltestlab.member.application.port.in.usecase.RegisterMemberUseCase;
import com.dochiri.tooltestlab.order.application.exception.CartSnapshotNotReadyException;
import com.dochiri.tooltestlab.order.application.exception.CheckoutAlreadyRequestedException;
import com.dochiri.tooltestlab.order.application.exception.EmptyCartException;
import com.dochiri.tooltestlab.order.application.port.in.command.PlaceOrderCommand;
import com.dochiri.tooltestlab.order.application.port.in.usecase.CancelOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.in.usecase.GetOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.in.usecase.PlaceOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.event.OrderCancelledIntegrationEvent;
import com.dochiri.tooltestlab.order.application.port.out.event.OrderPlacedIntegrationEvent;
import com.dochiri.tooltestlab.review.application.port.in.command.WriteReviewCommand;
import com.dochiri.tooltestlab.review.application.port.in.usecase.DeleteReviewUseCase;
import com.dochiri.tooltestlab.review.application.port.in.usecase.WriteReviewUseCase;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ActiveProfiles("test")
@SpringBootTest(classes = {ToolTestLabApplication.class, EventFlowTestBase.Config.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE, properties = {
        "messaging.outbox.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "logging.level.com.dochiri.tooltestlab=WARN",
        "logging.level.org.hibernate.SQL=OFF"
})
abstract class EventFlowTestBase {
    @Autowired RegisterProductUseCase registerProduct;
    @Autowired GetProductUseCase getProduct;
    @Autowired AddToCartUseCase addToCart;
    @Autowired RemoveFromCartUseCase removeFromCart;
    @Autowired GetCartUseCase getCart;
    @Autowired PlaceOrderUseCase placeOrder;
    @Autowired PublishProductSnapshotsUseCase publishProducts;
    @Autowired PublishCartSnapshotsUseCase publishCarts;
    @Autowired CancelOrderUseCase cancelOrder;
    @Autowired GetOrderUseCase getOrder;
    @Autowired RegisterMemberUseCase registerMember;
    @Autowired WriteReviewUseCase writeReview;
    @Autowired DeleteReviewUseCase deleteReview;
    @Autowired List<OutboxRelay> relays;
    @Autowired EntityManager entityManager;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired EventCodec codec;
    @Autowired RecordingBroker broker;
    @Autowired MutableClock clock;

    String memberId;

    @BeforeEach
    void reset() {
        broker.reset();
        clock.reset();
        transaction(() -> {
            entityManager.createNativeQuery("delete from product_categories").executeUpdate();
            entityManager.getMetamodel().getEntities().forEach(entity -> {
                var table = entity.getJavaType().getAnnotation(Table.class);
                entityManager.createNativeQuery("delete from " + table.name()).executeUpdate();
            });
            return null;
        });
        memberId = UuidCreator.getTimeOrderedEpoch().toString();
    }

    @Test
    @DisplayName("체크아웃과 취소는 상품 재고를 바꾸지 않는다")
    void checkout_and_cancellation_do_not_change_product_stock() {
        // given
        final String product = product(10);
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 2));
        drain();
        final var placed = placeOrder.place(command());

        // when & then
        assertEquals("PENDING", placed.status());
        assertEquals(10, stock(product));
        assertEquals(1, getCart.getCart(new GetCartQuery(memberId)).items().values().size());
        assertThrows(CheckoutAlreadyRequestedException.class, () -> placeOrder.place(command()));
        drain();
        assertEquals("PENDING", status(placed.publicId()));
        assertEquals(10, stock(product));
        assertTrue(getCart.getCart(new GetCartQuery(memberId)).items().values().isEmpty());
        cancelOrder.cancel(memberId, placed.publicId());
        assertEquals(10, stock(product));
        drain();
        assertEquals("CANCELLED", status(placed.publicId()));
        assertEquals(10, stock(product));
        final var cancellation = last(OrderCancelledIntegrationEvent.TYPE);
        broker.send(cancellation);
        assertEquals(10, stock(product));
        assertEquals(0, pending("order"));
        assertEquals(0, pending("catalog"));
        assertEquals(0, pending("cart"));
    }

    @Test
    @DisplayName("조회 모델이 없으면 명시적으로 알리고 빈 장바구니 스냅샷도 전달한다")
    void missing_projection_is_explicit_and_empty_cart_snapshot_can_be_delivered() {
        // given
        final String product = product(10);

        // when & then
        assertThrows(com.dochiri.tooltestlab.cart.application.exception.ProductSnapshotNotReadyException.class,
                () -> addToCart.add(new AddToCartCommand(memberId, product, 1)));
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 1));
        assertThrows(CartSnapshotNotReadyException.class, () -> placeOrder.place(command()));
        drain();
        removeFromCart.remove(new RemoveFromCartCommand(memberId, product));
        drain();
        assertThrows(EmptyCartException.class, () -> placeOrder.place(command()));
        assertEquals(0, pending("cart"));
    }

    @Test
    @DisplayName("전달 실패는 영속 Outbox로 재시도하고 커밋된 Inbox는 중복을 건너뛴다")
    void delivery_failure_retries_durable_outbox_and_committed_inbox_skips_duplicate() {
        // given
        final String product = product(10);
        broker.failAfter = true;
        relay("catalog");

        // when & then
        assertEquals(1, pending("catalog"));
        assertEquals(1, count("cart_inbox"));
        assertEquals(1, scalar("select attempts from catalog_outbox"));
        final var sent = last(ProductChangedIntegrationEvent.TYPE);
        broker.failAfter = false;
        clock.advance(2);
        relay("catalog");
        assertEquals(0, pending("catalog"));
        assertEquals(1, count("cart_inbox"));
        assertEquals(sent.eventId(), last(ProductChangedIntegrationEvent.TYPE).eventId());
        addToCart.add(new AddToCartCommand(memberId, product, 1));
        assertEquals(1, getCart.getCart(new GetCartQuery(memberId)).items().values().size());
    }

    @Test
    @DisplayName("전달 전 실패는 Inbox를 비우고 재시도 전까지 이후 메시지를 막는다")
    void failure_before_delivery_leaves_inbox_empty_and_blocks_later_messages_until_retry() {
        // given
        product(10);
        product(10);
        broker.failBefore = true;

        // when & then
        assertEquals(0, relay("catalog"));
        assertEquals(2, pending("catalog"));
        assertEquals(0, count("cart_inbox"));
        broker.failBefore = false;
        assertEquals(0, relay("catalog"));
        clock.advance(2);
        assertEquals(2, relay("catalog"));
        assertEquals(2, count("cart_inbox"));
    }

    @Test
    @DisplayName("동시 체크아웃 요청은 같은 장바구니를 한 번만 선점한다")
    void concurrent_checkout_requests_claim_the_same_cart_only_once() throws Exception {
        // given
        final String product = product(10);
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 2));
        drain();
        final Callable<Boolean> submit = () -> {
            try {
                placeOrder.place(command());
                return true;
            } catch (final CheckoutAlreadyRequestedException expected) {
                return false;
            }
        };

        // when & then
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final var results = executor.invokeAll(List.of(submit, submit, submit, submit));
            int successful = 0;
            for (final var result : results) {
                if (result.get()) {
                    successful++;
                }
            }
            assertEquals(1, successful);
        }
        assertEquals(1, count("orders"));
        drain();
        assertEquals(10, stock(product));
    }

    @Test
    @DisplayName("없는 조회 모델은 소유 BC의 이벤트로 다시 만들 수 있다")
    void missing_read_models_can_be_rebuilt_using_events_from_their_owners() {
        // given
        final String product = product(10);
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 2));
        drain();
        transaction(() -> {
            entityManager.createNativeQuery("delete from cart_product_snapshots").executeUpdate();
            entityManager.createNativeQuery("delete from order_cart_snapshots").executeUpdate();
            return null;
        });

        // when & then
        assertEquals(1, publishProducts.publishPage(0, 100));
        assertEquals(1, publishCarts.publishPage(0, 100));
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 1));
        drain();
        final var placed = placeOrder.place(command());
        assertEquals(3, placed.items().values().getFirst().quantity());
    }

    @Test
    @DisplayName("중복된 주문 이벤트는 재고를 바꾸거나 체크아웃을 반복하지 않는다")
    void duplicated_order_events_do_not_change_stock_or_repeat_cart_checkout() {
        // given
        final String product = product(10);
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 2));
        drain();
        final var placed = placeOrder.place(command());
        relay("order");
        final var event = last(OrderPlacedIntegrationEvent.TYPE);

        // when & then
        assertEquals(10, stock(product));
        assertTrue(getCart.getCart(new GetCartQuery(memberId)).items().values().isEmpty());
        addToCart.add(new AddToCartCommand(memberId, product, 1));
        broker.send(event);
        broker.send(new MessageEnvelope(UuidCreator.getTimeOrderedEpoch().toString(), event.source(),
                event.eventType(), event.aggregateId(), event.sequence() + 1, event.occurredAt(), event.payload()));
        assertEquals(10, stock(product));
        assertEquals(1, getCart.getCart(new GetCartQuery(memberId)).items().values().getFirst().quantity());
        drain();
        cancelOrder.cancel(memberId, placed.publicId());
        drain();
        final var cancellation = last(OrderCancelledIntegrationEvent.TYPE);
        broker.send(new MessageEnvelope(UuidCreator.getTimeOrderedEpoch().toString(), cancellation.source(),
                cancellation.eventType(), cancellation.aggregateId(), cancellation.sequence() + 1,
                cancellation.occurredAt(), cancellation.payload()));
        assertEquals(10, stock(product));
    }

    @Test
    @DisplayName("늦게 도착한 주문 이벤트는 장바구니 변경을 지우지 않고 오래된 스냅샷은 새 스냅샷을 덮어쓰지 않는다")
    void late_order_delivery_does_not_erase_cart_changes_and_old_snapshot_does_not_overwrite_new() {
        // given
        final String product = product(10);
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 2));
        drain();
        final var oldCart = broker.messages.stream().filter(m -> m.source().equals("cart")).reduce((a, b) -> b).orElseThrow();
        final var placed = placeOrder.place(command());
        addToCart.add(new AddToCartCommand(memberId, product, 1));
        drain();

        // when & then
        assertEquals("PENDING", status(placed.publicId()));
        assertEquals(3, getCart.getCart(new GetCartQuery(memberId)).items().values().getFirst().quantity());
        broker.send(new MessageEnvelope(UuidCreator.getTimeOrderedEpoch().toString(), oldCart.source(),
                oldCart.eventType(), oldCart.aggregateId(), oldCart.sequence(), oldCart.occurredAt(), oldCart.payload()));
        final var second = placeOrder.place(command());
        assertEquals(3, second.items().values().getFirst().quantity());
    }

    @Test
    @DisplayName("애그리거트 변경과 Outbox 추가는 함께 롤백된다")
    void aggregate_changes_and_outbox_append_rollback_together() {
        // given
        final String product = product(10);
        drain();
        final long outboxCount = count("cart_outbox");

        // when & then
        assertThrows(IllegalStateException.class, () -> transaction(() -> {
            addToCart.add(new AddToCartCommand(memberId, product, 2));
            throw new IllegalStateException("rollback");
        }));
        assertTrue(getCart.getCart(new GetCartQuery(memberId)).items().values().isEmpty());
        assertEquals(outboxCount, count("cart_outbox"));
    }

    @Test
    @DisplayName("회원과 리뷰는 비밀번호 없이 공개 계약을 발행한다")
    void member_and_review_publish_public_contracts_without_passwords() {
        // given
        final var member = registerMember.register(new RegisterMemberCommand("events@test.com", "1234", "회원"));
        final String product = product(10);
        final var review = writeReview.write(new WriteReviewCommand(member.publicId(), product, 5, "좋은 상품입니다."));
        deleteReview.delete(review.publicId(), member.publicId());
        drain();

        // when & then
        assertEquals(1, count("member_outbox"));
        assertEquals(2, count("review_outbox"));
        final var memberMessage = broker.messages.stream().filter(m -> m.source().equals("member")).findFirst().orElseThrow();
        assertFalse(memberMessage.payload().contains("password"));
        assertFalse(memberMessage.payload().contains("1234"));
        assertEquals(0, pending("member"));
        assertEquals(0, pending("review"));
    }

    String product(final int stock) {
        return registerProduct.register(new RegisterProductCommand(
                "상품" + UuidCreator.getTimeOrderedEpoch(), "이벤트 테스트 상품 상세 설명입니다.", 100, stock, new CategoryPublicIds(Set.of()))).publicId();
    }

    PlaceOrderCommand command() {
        return new PlaceOrderCommand(memberId, "홍길동", "010-1234-5678", "서울시 주소", "12345");
    }

    int stock(final String productId) {
        return getProduct.getProduct(new GetProductQuery(productId)).stock();
    }

    String status(final String orderId) {
        return getOrder.getOrder(new GetOrderQuery(memberId, orderId)).status();
    }

    void drain() {
        for (int pass = 0; pass < 20; pass++) {
            int delivered = 0;
            for (final var relay : relays) {
                delivered += relay.relay();
            }
            if (delivered == 0) {
                return;
            }
        }
        fail("이벤트 전달이 수렴하지 않았습니다.");
    }

    int relay(final String bc) {
        return relays.stream().filter(r -> r.getClass().getSimpleName().startsWith(
                bc.substring(0, 1).toUpperCase() + bc.substring(1))).findFirst().orElseThrow().relay();
    }

    MessageEnvelope last(final String eventType) {
        return broker.messages.stream().filter(m -> m.eventType().equals(eventType)).reduce((a, b) -> b).orElseThrow();
    }

    MessageEnvelope message(final String source, final IntegrationEvent event, final long sequence) {
        return new MessageEnvelope(UuidCreator.getTimeOrderedEpoch().toString(), source, event.eventType(),
                event.aggregateId(), sequence, clock.instant(), codec.encode(event));
    }

    long count(final String table) {
        return scalar("select count(*) from " + table);
    }

    long pending(final String bc) {
        return scalar("select count(*) from " + bc + "_outbox where published_at is null");
    }

    long scalar(final String sql) {
        return transaction(() -> ((Number) entityManager.createNativeQuery(sql).getSingleResult()).longValue());
    }

    <T> T transaction(final Supplier<T> work) {
        return new TransactionTemplate(transactionManager).execute(status -> work.get());
    }

    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        RecordingBroker recordingBroker(final ApplicationEventPublisher publisher) {
            return new RecordingBroker(publisher);
        }

        @Bean
        @Primary
        MutableClock mutableClock() {
            return new MutableClock();
        }
    }

    static final class RecordingBroker implements MessageBroker {
        private final ApplicationEventPublisher publisher;
        final List<MessageEnvelope> messages = new CopyOnWriteArrayList<>();
        volatile boolean failBefore;
        volatile boolean failAfter;

        RecordingBroker(final ApplicationEventPublisher publisher) {
            this.publisher = publisher;
        }

        void reset() {
            messages.clear();
            failBefore = false;
            failAfter = false;
        }

        @Override
        public CompletionStage<Void> send(final MessageEnvelope message) {
            if (failBefore) {
                throw new IllegalStateException("전송 전 실패");
            }
            messages.add(message);
            publisher.publishEvent(message);
            if (failAfter) {
                throw new IllegalStateException("수신자 커밋 후 응답 실패");
            }
            return CompletableFuture.completedFuture(null);
        }
    }

    static final class MutableClock extends Clock {
        private Instant now = Instant.parse("2026-10-04T00:00:00Z");

        void reset() {
            now = Instant.parse("2026-10-04T00:00:00Z");
        }

        void advance(final long seconds) {
            now = now.plusSeconds(seconds);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(final ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return now;
        }
    }
}
