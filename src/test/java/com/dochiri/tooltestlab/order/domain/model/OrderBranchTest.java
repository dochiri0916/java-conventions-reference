package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderStatusException;
import com.dochiri.tooltestlab.shared.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderBranchTest {

    private Order pending;

    @BeforeEach
    void setUp() {
        var line = new OrderLine(new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("상품"),
                new Quantity(1), Money.of(1000));
        var address = new ShippingAddress(new RecipientName("홍길동"), new Phone("01012345678"),
                new StreetAddress("서울시"), new ZipCode("12345"));
        pending = Order.create(new MemberId("m1"), OrderLines.from(List.of(line)), address);
    }

    @Test
    @DisplayName("pay 성공")
    void paySuccess() {
            // given

            // when

            // then

        var paid = pending.pay();
        assertThat(paid.status()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    @DisplayName("pay 실패 - PENDING이 아니면 예외")
    void payFail() {
            // given

            // when

            // then

        var paid = pending.pay();
        assertThatThrownBy(paid::pay).isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    @DisplayName("startShipping 성공")
    void startShippingSuccess() {
            // given

            // when

            // then

        var shipping = pending.pay().startShipping();
        assertThat(shipping.status()).isEqualTo(OrderStatus.SHIPPING);
    }

    @Test
    @DisplayName("startShipping 실패 - PAID가 아니면 예외")
    void startShippingFail() {
            // given

            // when

            // then

        assertThatThrownBy(pending::startShipping).isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    @DisplayName("deliver 성공")
    void deliverSuccess() {
            // given

            // when

            // then

        var delivered = pending.pay().startShipping().deliver();
        assertThat(delivered.status()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    @DisplayName("deliver 실패 - SHIPPING이 아니면 예외")
    void deliverFail() {
            // given

            // when

            // then

        assertThatThrownBy(pending::deliver).isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    @DisplayName("cancel 성공 - PENDING 상태")
    void cancelPending() {
            // given

            // when

            // then

        var cancelled = pending.cancel();
        assertThat(cancelled.status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("cancel 성공 - PAID 상태")
    void cancelPaid() {
            // given

            // when

            // then

        var cancelled = pending.pay().cancel();
        assertThat(cancelled.status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("cancel 실패 - DELIVERED 상태")
    void cancelDeliveredFail() {
            // given

            // when

            // then

        var delivered = pending.pay().startShipping().deliver();
        assertThatThrownBy(delivered::cancel).isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    @DisplayName("cancel 실패 - 이미 CANCELLED 상태")
    void cancelCancelledFail() {
            // given

            // when

            // then

        var cancelled = pending.cancel();
        assertThatThrownBy(cancelled::cancel).isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    @DisplayName("equals - 같은 ID면 true")
    void equalsSameId() {
            // given

            // when

            // then

        assertThat(pending.equals(pending)).isTrue();
    }

    @Test
    @DisplayName("equals - 다른 객체는 false")
    void equalsDifferentType() {
            // given

            // when

            // then

        assertThat(pending.equals("string")).isFalse();
    }

    @Test
    @DisplayName("equals - 다른 인스턴스 같은 ID")
    void equalsDifferentInstanceSameId() {
            // given

            // when

            // then

        var line = new OrderLine(new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("상품"),
                new Quantity(1), Money.of(1000));
        var address = new ShippingAddress(new RecipientName("홍길동"), new Phone("01012345678"),
                new StreetAddress("서울시"), new ZipCode("12345"));
        var other = new Order(pending.id(), new MemberId("other"), OrderLines.from(List.of(line)),
                OrderStatus.DELIVERED, address, Money.of(5000));
        assertThat(pending.equals(other)).isTrue();
    }

    @Test
    @DisplayName("hashCode는 id 기반")
    void hashCodeBasedOnId() {
            // given

            // when

            // then

        assertThat(pending.hashCode()).isEqualTo(pending.id().hashCode());
    }

    @Test
    @DisplayName("OrderLines - 빈 목록 예외")
    void orderLinesEmpty() {
            // given

            // when

            // then

        assertThatThrownBy(() -> OrderLines.from(List.of()))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("OrderLines - fromArray null 예외")
    void orderLinesFromArrayNull() {
            // given

            // when

            // then

        assertThatThrownBy(() -> OrderLines.fromArray((OrderLine[]) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("OrderLines totalAmount 계산")
    void orderLinesTotalAmount() {
            // given

            // when

            // then

        var line1 = new OrderLine(new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("A"),
                new Quantity(2), Money.of(1000));
        var line2 = new OrderLine(new com.dochiri.tooltestlab.product.domain.model.ProductId("p2"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("B"),
                new Quantity(3), Money.of(500));
        var lines = OrderLines.from(List.of(line1, line2));
        assertThat(lines.totalAmount()).isEqualTo(Money.of(3500));
        assertThat(lines.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("OrderLines - null 목록이면 NullPointerException")
    void orderLinesNullValues() {
            // given

            // when

            // then
        assertThatThrownBy(() -> new OrderLines(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("OrderLines - null 요소가 포함되면 예외")
    void orderLinesNullElement() {
            // given
        var list = new java.util.ArrayList<OrderLine>();
        list.add(new OrderLine(new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("A"),
                new Quantity(1), Money.of(1000)));
        list.add(null);

            // when

            // then
        assertThatThrownBy(() -> OrderLines.from(list))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("OrderLines - 최대 개수 초과 예외")
    void orderLinesMaxExceeded() {
            // given
        var list = new java.util.ArrayList<OrderLine>();
        for (int i = 0; i < 101; i++) {
            list.add(new OrderLine(new com.dochiri.tooltestlab.product.domain.model.ProductId("p" + i),
                    new com.dochiri.tooltestlab.product.domain.model.ProductName("상품"),
                    new Quantity(1), Money.of(1000)));
        }

            // when

            // then
        assertThatThrownBy(() -> OrderLines.from(list))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("OrderLines - containsProduct 정상")
    void orderLinesContainsProduct() {
            // given
        var line = new OrderLine(new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("A"),
                new Quantity(1), Money.of(1000));
        var lines = OrderLines.from(List.of(line));

            // when

            // then
        assertThat(lines.containsProduct(new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"))).isTrue();
        assertThat(lines.containsProduct(new com.dochiri.tooltestlab.product.domain.model.ProductId("p2"))).isFalse();
    }

    @Test
    @DisplayName("OrderPlacedItems - null 목록이면 NullPointerException")
    void orderPlacedItemsNullValues() {
            // given

            // when

            // then
        assertThatThrownBy(() -> new OrderPlacedItems(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("OrderPlacedItems - null 요소가 포함되면 예외")
    void orderPlacedItemsNullElement() {
            // given
        var list = new java.util.ArrayList<com.dochiri.tooltestlab.order.domain.event.OrderPlacedEvent.ItemDetail>();
        list.add(new com.dochiri.tooltestlab.order.domain.event.OrderPlacedEvent.ItemDetail(
                new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new com.dochiri.tooltestlab.product.domain.model.ProductName("상품"),
                new Quantity(1), Money.of(1000)));
        list.add(null);

            // when

            // then
        assertThatThrownBy(() -> new OrderPlacedItems(list))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("OrderCancelledItems - null 목록이면 NullPointerException")
    void orderCancelledItemsNullValues() {
            // given

            // when

            // then
        assertThatThrownBy(() -> new OrderCancelledItems(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("OrderCancelledItems - null 요소가 포함되면 예외")
    void orderCancelledItemsNullElement() {
            // given
        var list = new java.util.ArrayList<com.dochiri.tooltestlab.order.domain.event.OrderCancelledEvent.ItemDetail>();
        list.add(new com.dochiri.tooltestlab.order.domain.event.OrderCancelledEvent.ItemDetail(
                new com.dochiri.tooltestlab.product.domain.model.ProductId("p1"),
                new Quantity(1)));
        list.add(null);

            // when

            // then
        assertThatThrownBy(() -> new OrderCancelledItems(list))
                .isInstanceOf(RuntimeException.class);
    }
}
