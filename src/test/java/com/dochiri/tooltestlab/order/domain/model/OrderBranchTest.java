package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;
import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderBranchTest {

    private Order pending;

    @BeforeEach
    void setUp() {
        var line = new OrderLine(
                new ProductId("p1"),
                new ProductName("상품"),
                new Quantity(1),
                new Money(1000));
        var address = new ShippingAddress(new RecipientName("홍길동"), new Phone("01012345678"),
                new StreetAddress("서울시"), new ZipCode("12345"));
        pending = Order.create(new MemberId("m1"), OrderLines.from(List.of(line)), address);
    }

    @Test
    @DisplayName("pay 성공")
    void paySuccess() {
        // given
        var order = pending;

        // when
        var paid = order.pay();

        // then
        assertThat(paid.status()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    @DisplayName("pay 실패 - PENDING이 아니면 예외")
    void payFail() {
        // given
        var paid = pending.pay();

        // when & then
        assertThatThrownBy(paid::pay).isInstanceOf(OrderDomainException.class);
    }

    @Test
    @DisplayName("startShipping 성공")
    void startShippingSuccess() {
        // given
        var paid = pending.pay();

        // when
        var shipping = paid.startShipping();

        // then
        assertThat(shipping.status()).isEqualTo(OrderStatus.SHIPPING);
    }

    @Test
    @DisplayName("startShipping 실패 - PAID가 아니면 예외")
    void startShippingFail() {
        // given
        var order = pending;

        // when & then
        assertThatThrownBy(order::startShipping).isInstanceOf(OrderDomainException.class);
    }

    @Test
    @DisplayName("deliver 성공")
    void deliverSuccess() {
        // given
        var shipping = pending.pay().startShipping();

        // when
        var delivered = shipping.deliver();

        // then
        assertThat(delivered.status()).isEqualTo(OrderStatus.DELIVERED);
    }

    @Test
    @DisplayName("deliver 실패 - SHIPPING이 아니면 예외")
    void deliverFail() {
        // given
        var order = pending;

        // when & then
        assertThatThrownBy(order::deliver).isInstanceOf(OrderDomainException.class);
    }

    @Test
    @DisplayName("cancel 성공 - PENDING 상태")
    void cancelPending() {
        // given
        var order = pending;

        // when
        var cancelled = order.cancel();

        // then
        assertThat(cancelled.status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("cancel 성공 - PAID 상태")
    void cancelPaid() {
        // given
        var paid = pending.pay();

        // when
        var cancelled = paid.cancel();

        // then
        assertThat(cancelled.status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("cancel 실패 - DELIVERED 상태")
    void cancelDeliveredFail() {
        // given
        var delivered = pending.pay().startShipping().deliver();

        // when & then
        assertThatThrownBy(delivered::cancel).isInstanceOf(OrderDomainException.class);
    }

    @Test
    @DisplayName("cancel 실패 - 이미 CANCELLED 상태")
    void cancelCancelledFail() {
        // given
        var cancelled = pending.cancel();

        // when & then
        assertThatThrownBy(cancelled::cancel).isInstanceOf(OrderDomainException.class);
    }

    @Test
    @DisplayName("equals - 같은 ID면 true")
    void equalsSameId() {
        // given
        var order = pending;

        // when
        var result = order.equals(order);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("equals - 다른 객체는 false")
    void equalsDifferentType() {
        // given
        var otherObject = new Object();

        // when
        var result = pending.equals(otherObject);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("equals - 다른 인스턴스 같은 ID")
    void equalsDifferentInstanceSameId() {
        // given
        var line = new OrderLine(
                new ProductId("p1"),
                new ProductName("상품"),
                new Quantity(1),
                new Money(1000));
        var address = new ShippingAddress(new RecipientName("홍길동"), new Phone("01012345678"),
                new StreetAddress("서울시"), new ZipCode("12345"));
        var other = Order.restore(
                pending.id(),
                new MemberId("other"),
                OrderLines.from(List.of(line)),
                OrderStatus.DELIVERED,
                address,
                new Money(5000)
        );

        // when
        var result = pending.equals(other);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("hashCode는 id 기반")
    void hashCodeBasedOnId() {
        // given
        var order = pending;

        // when
        var hash = order.hashCode();

        // then
        assertThat(hash).isEqualTo(order.id().hashCode());
    }

    @Test
    @DisplayName("OrderLines - 빈 목록 예외")
    void orderLinesEmpty() {
        // given
        var emptyList = List.<OrderLine>of();

        // when & then
        assertThatThrownBy(() -> OrderLines.from(emptyList))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("OrderLines - fromArray null 예외")
    void orderLinesFromArrayNull() {
        // given
        var expectedException = OrderDomainException.class;

        // when & then
        assertThatThrownBy(() -> OrderLines.fromArray((OrderLine[]) null))
                .isInstanceOf(expectedException);
    }

    @Test
    @DisplayName("OrderLines totalAmount 계산")
    void orderLinesTotalAmount() {
        // given
        var line1 = new OrderLine(
                new ProductId("p1"),
                new ProductName("A"),
                new Quantity(2),
                new Money(1000));

        var line2 = new OrderLine(
                new ProductId("p2"),
                new ProductName("B"),
                new Quantity(3),
                new Money(500));

        var lines = OrderLines.from(List.of(line1, line2));

        // when
        var total = lines.totalAmount();

        // then
        assertThat(total).isEqualTo(new Money(3500));
        assertThat(lines.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("OrderLines - null 목록이면 InvalidOrderLinesException")
    void orderLinesNullValues() {
        // given
        var expectedException = OrderDomainException.class;

        // when & then
        assertThatThrownBy(() -> new OrderLines(null))
                .isInstanceOf(expectedException);
    }

    @Test
    @DisplayName("OrderLines - null 요소가 포함되면 예외")
    void orderLinesNullElement() {
        // given
        var list = new ArrayList<OrderLine>();
        list.add(
                new OrderLine(
                        new ProductId("p1"),
                        new ProductName("A"),
                        new Quantity(1),
                        new Money(1000)
                )
        );
        list.add(null);

        // when & then
        assertThatThrownBy(() -> OrderLines.from(list))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("OrderLines - 최대 개수 초과 예외")
    void orderLinesMaxExceeded() {
        // given
        var list = new ArrayList<OrderLine>();
        for (int i = 0; i < 101; i++) {
            list.add(
                    new OrderLine(
                            new ProductId("p" + i),
                            new ProductName("상품"),
                            new Quantity(1),
                            new Money(1000)
                    )
            );
        }

        // when & then
        assertThatThrownBy(() -> OrderLines.from(list))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("OrderLines - containsProduct 정상")
    void orderLinesContainsProduct() {
        // given
        var line = new OrderLine(
                new ProductId("p1"),
                new ProductName("A"),
                new Quantity(1),
                new Money(1000)
        );

        var lines = OrderLines.from(List.of(line));
        var existingProductId = new ProductId("p1");
        var nonExistingProductId = new ProductId("p2");

        // when
        var containsExisting = lines.containsProduct(existingProductId);
        var containsNonExisting = lines.containsProduct(nonExistingProductId);

        // then
        assertThat(containsExisting).isTrue();
        assertThat(containsNonExisting).isFalse();
    }

    @Test
    @DisplayName("OrderLines - containsProduct에 null 전달 시 예외")
    void orderLinesContainsProductNull() {
        // given
        var line = new OrderLine(
                new ProductId("p1"),
                new ProductName("A"),
                new Quantity(1),
                new Money(1000)
        );

        var lines = OrderLines.from(List.of(line));

        // when & then
        assertThatThrownBy(() -> lines.containsProduct(null))
                .isInstanceOf(com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException.class);
    }

    @Test
    @DisplayName("OrderPlacedItems - null 목록이면 InvalidOrderLinesException")
    void orderPlacedItemsNullValues() {
        // given
        var expectedException = OrderDomainException.class;

        // when & then
        assertThatThrownBy(() -> new OrderPlacedItems(null))
                .isInstanceOf(expectedException);
    }

    @Test
    @DisplayName("OrderPlacedItems - null 요소가 포함되면 예외")
    void orderPlacedItemsNullElement() {
        // given
        var list = new ArrayList<OrderPlaced.ItemDetail>();
        list.add(
                new OrderPlaced.ItemDetail(
                        new ProductId("p1"),
                        new ProductName("상품"),
                        new Quantity(1),
                        new Money(1000)
                )
        );

        list.add(null);

        // when & then
        assertThatThrownBy(() -> new OrderPlacedItems(list))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("OrderCancelledItems - null 목록이면 InvalidOrderLinesException")
    void orderCancelledItemsNullValues() {
        // given
        var expectedException = OrderDomainException.class;

        // when & then
        assertThatThrownBy(() -> new OrderCancelledItems(null))
                .isInstanceOf(expectedException);
    }

    @Test
    @DisplayName("OrderCancelledItems - null 요소가 포함되면 예외")
    void orderCancelledItemsNullElement() {
        // given
        var list = new java.util.ArrayList<com.dochiri.tooltestlab.order.domain.event.OrderCancelled.ItemDetail>();
        list.add(new com.dochiri.tooltestlab.order.domain.event.OrderCancelled.ItemDetail(
                new ProductId("p1"),
                new Quantity(1)));
        list.add(null);

        // when & then
        assertThatThrownBy(() -> new OrderCancelledItems(list))
                .isInstanceOf(RuntimeException.class);
    }
}
