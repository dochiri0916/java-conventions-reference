package com.dochiri.tooltestlab.messaging;

import com.dochiri.tooltestlab.ToolTestLabApplication;
import com.dochiri.tooltestlab.cart.application.port.in.*;
import com.dochiri.tooltestlab.catalog.application.port.in.*;
import com.dochiri.tooltestlab.catalog.application.port.out.event.*;
import com.dochiri.tooltestlab.catalog.product.application.port.in.*;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.global.messaging.*;
import com.dochiri.tooltestlab.member.application.port.in.*;
import com.dochiri.tooltestlab.order.application.exception.*;
import com.dochiri.tooltestlab.order.application.port.in.*;
import com.dochiri.tooltestlab.order.application.port.out.event.*;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.Order;
import com.dochiri.tooltestlab.order.domain.OrderStatus;
import com.dochiri.tooltestlab.order.domain.vo.*;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.review.application.port.in.*;
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
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

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
    @Autowired IncreaseProductStockUseCase increaseStock;
    @Autowired AddToCartUseCase addToCart;
    @Autowired RemoveFromCartUseCase removeFromCart;
    @Autowired GetCartUseCase getCart;
    @Autowired PlaceOrderUseCase placeOrder;
    @Autowired OrderRepositoryPort orderRepositoryPort;
    @Autowired PublishProductSnapshotsUseCase publishProducts;
    @Autowired PublishCartSnapshotsUseCase publishCarts;
    @Autowired CancelOrderUseCase cancelOrder;
    @Autowired GetOrderUseCase getOrder;
    @Autowired RegisterMemberUseCase registerMember;
    @Autowired WriteReviewUseCase writeReview;
    @Autowired DeleteReviewUseCase deleteReview;
    @Autowired ReserveOrderStockUseCase reserveStock;
    @Autowired ReleaseOrderStockUseCase releaseStock;
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
                final var table = entity.getJavaType().getAnnotation(Table.class);
                entityManager.createNativeQuery("delete from " + table.name()).executeUpdate();
            });
            return null;
        });
        memberId = UuidCreator.getTimeOrderedEpoch().toString();
    }

    @Test
    void checkoutAndCancellationUseCommittedEventsAndRestoreReservedStockOnce() {
        final String product = product(10);
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 2));
        drain();

        final var placed = placeOrder.place(command());
        assertEquals("STOCK_PENDING", placed.status());
        assertEquals(10, stock(product));
        assertEquals(1, getCart.getCart(memberId).items().size());
        assertThrows(CheckoutAlreadyRequestedException.class, () -> placeOrder.place(command()));

        drain();
        assertEquals("PENDING", status(placed.publicId()));
        assertEquals(8, stock(product));
        assertTrue(getCart.getCart(memberId).items().isEmpty());
        assertEquals(1, count("catalog_stock_reservations"));

        cancelOrder.cancel(memberId, placed.publicId());
        assertEquals(8, stock(product));
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
    void missingProjectionIsExplicitAndEmptyCartSnapshotCanBeDelivered() {
        final String product = product(10);
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
    void stockFailureDoesNotPartiallyReserveAndAllowsRetryingTheCart() {
        final String first = product(10);
        final String second = product(1);
        drain();
        addToCart.add(new AddToCartCommand(memberId, first, 2));
        addToCart.add(new AddToCartCommand(memberId, second, 2));
        drain();

        final var placed = placeOrder.place(command());
        drain();
        assertEquals("REJECTED", status(placed.publicId()));
        assertEquals(10, stock(first));
        assertEquals(1, stock(second));
        assertEquals(2, getCart.getCart(memberId).items().size());

        increaseStock.increase(second, 4);
        drain();
        final var retried = placeOrder.place(command());
        drain();
        assertEquals("PENDING", status(retried.publicId()));
        assertEquals(8, stock(first));
        assertEquals(3, stock(second));
    }

    @Test
    void deliveryFailureRetriesDurableOutboxAndCommittedInboxSkipsDuplicate() {
        final String product = product(10);
        broker.failAfter = true;
        relay("catalog");
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
        assertEquals(1, getCart.getCart(memberId).items().size());
    }

    @Test
    void failureBeforeDeliveryLeavesInboxEmptyAndBlocksLaterMessagesUntilRetry() {
        product(10);
        product(10);
        broker.failBefore = true;
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
    void failedConsumerRollsBackInboxAndCanHandleSameEventAfterRepair() {
        final String orderId = UuidCreator.getTimeOrderedEpoch().toString();
        final var message = message("catalog", new StockReservedIntegrationEvent(orderId), 1);
        assertThrows(RuntimeException.class, () -> broker.send(message));
        assertEquals(0, count("order_inbox"));
        final String product = product(10);
        drain();
        transaction(() -> orderRepositoryPort.create(Order.restore(
                OrderId.parse(orderId), MemberReferenceId.parse(memberId),
                CartReferenceId.parse(UuidCreator.getTimeOrderedEpoch().toString()), 1,
                OrderLines.of(OrderLine.of(ProductReferenceId.parse(product), ProductName.of("상품"),
                        Quantity.of(1), Money.of(100))), OrderStatus.STOCK_PENDING,
                ShippingAddress.of(RecipientName.of("홍길동"), Phone.of("010-1234-5678"),
                        StreetAddress.of("서울시 주소"), ZipCode.of("12345")), Money.of(100))));
        broker.send(message);
        assertEquals(1, count("order_inbox"));
        assertEquals("PENDING", status(orderId));
        broker.send(message);
        assertEquals(1, count("order_inbox"));
    }

    @Test
    void concurrentCheckoutRequestsClaimTheSameCartOnlyOnce() throws Exception {
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
        assertEquals(8, stock(product));
    }

    @Test
    void missingReadModelsCanBeRebuiltUsingEventsFromTheirOwners() {
        final String product = product(10);
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 2));
        drain();
        transaction(() -> {
            entityManager.createNativeQuery("delete from cart_product_snapshots").executeUpdate();
            entityManager.createNativeQuery("delete from order_cart_snapshots").executeUpdate();
            return null;
        });
        assertEquals(1, publishProducts.publishPage(0, 100));
        assertEquals(1, publishCarts.publishPage(0, 100));
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 1));
        drain();
        final var placed = placeOrder.place(command());
        assertEquals(3, placed.items().getFirst().quantity());
    }

    @Test
    void duplicatedOrderEventsAndNewEventIdsForSameOrderDoNotDoubleReserveOrRelease() {
        final String product = product(10);
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 2));
        drain();
        final var placed = placeOrder.place(command());
        relay("order");
        final var event = last(OrderPlacedIntegrationEvent.TYPE);
        assertEquals(8, stock(product));
        broker.send(event);
        broker.send(new MessageEnvelope(UuidCreator.getTimeOrderedEpoch().toString(), event.source(),
                event.eventType(), event.aggregateId(), event.sequence() + 1, event.occurredAt(), event.payload()));
        assertEquals(8, stock(product));
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
    void cancellationBeforeStockReplyNeverConfirmsOrClearsTheCart() {
        final String product = product(10);
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 2));
        drain();
        final var placed = placeOrder.place(command());
        cancelOrder.cancel(memberId, placed.publicId());
        drain();
        assertEquals("CANCELLED", status(placed.publicId()));
        assertEquals(10, stock(product));
        assertEquals(1, getCart.getCart(memberId).items().size());
        assertTrue(broker.messages.stream().noneMatch(m -> m.eventType().equals(OrderConfirmedIntegrationEvent.TYPE)));
    }

    @Test
    void cancellationArrivingBeforePlacementPreventsLateReservation() {
        final String product = product(10);
        drain();
        final String orderId = UuidCreator.getTimeOrderedEpoch().toString();
        releaseStock.release(new ReleaseOrderStockCommand(orderId));
        reserveStock.reserve(new ReserveOrderStockCommand(orderId, List.of(new ReserveOrderStockCommand.Item(product, 2))));
        assertEquals(10, stock(product));
        assertEquals("RELEASED", transaction(() -> entityManager.createNativeQuery(
                "select status from catalog_stock_reservations where order_reference_id = :id")
                .setParameter("id", orderId).getSingleResult()));
    }

    @Test
    void lateConfirmationDoesNotEraseCartChangesAndOldSnapshotDoesNotOverwriteNew() {
        final String product = product(10);
        drain();
        addToCart.add(new AddToCartCommand(memberId, product, 2));
        drain();
        final var oldCart = broker.messages.stream().filter(m -> m.source().equals("cart")).reduce((a, b) -> b).orElseThrow();
        final var placed = placeOrder.place(command());
        addToCart.add(new AddToCartCommand(memberId, product, 1));
        drain();
        assertEquals("PENDING", status(placed.publicId()));
        assertEquals(3, getCart.getCart(memberId).items().getFirst().quantity());
        broker.send(new MessageEnvelope(UuidCreator.getTimeOrderedEpoch().toString(), oldCart.source(),
                oldCart.eventType(), oldCart.aggregateId(), oldCart.sequence(), oldCart.occurredAt(), oldCart.payload()));
        final var second = placeOrder.place(command());
        assertEquals(3, second.items().getFirst().quantity());
    }

    @Test
    void aggregateChangesAndOutboxAppendRollbackTogether() {
        final String product = product(10);
        drain();
        final long outboxCount = count("cart_outbox");
        assertThrows(IllegalStateException.class, () -> transaction(() -> {
            addToCart.add(new AddToCartCommand(memberId, product, 2));
            throw new IllegalStateException("rollback");
        }));
        assertTrue(getCart.getCart(memberId).items().isEmpty());
        assertEquals(outboxCount, count("cart_outbox"));
    }

    @Test
    void memberAndReviewPublishPublicContractsWithoutPasswords() {
        final var member = registerMember.register(new RegisterMemberCommand("events@test.com", "1234", "회원"));
        final String product = product(10);
        final var review = writeReview.write(new WriteReviewCommand(member.publicId(), product, 5, "좋은 상품입니다."));
        deleteReview.delete(review.publicId(), member.publicId());
        drain();
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
                "상품" + UuidCreator.getTimeOrderedEpoch(), "이벤트 테스트 상품 상세 설명입니다.", 100, stock, Set.of())).publicId();
    }

    PlaceOrderCommand command() {
        return new PlaceOrderCommand(memberId, "홍길동", "010-1234-5678", "서울시 주소", "12345");
    }

    int stock(final String productId) {
        return getProduct.getProduct(productId).stock();
    }

    String status(final String orderId) {
        return getOrder.getOrder(memberId, orderId).status();
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
        public void send(final MessageEnvelope message) {
            if (failBefore) {
                throw new IllegalStateException("전송 전 실패");
            }
            messages.add(message);
            publisher.publishEvent(message);
            if (failAfter) {
                throw new IllegalStateException("수신자 커밋 후 응답 실패");
            }
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
