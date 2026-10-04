package com.dochiri.tooltestlab.order.domain;

import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderStatusException;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.CartReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.OrderLine;
import com.dochiri.tooltestlab.order.domain.vo.OrderLines;
import com.dochiri.tooltestlab.order.domain.vo.Phone;
import com.dochiri.tooltestlab.order.domain.vo.ProductName;
import com.dochiri.tooltestlab.order.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.RecipientName;
import com.dochiri.tooltestlab.order.domain.vo.ShippingAddress;
import com.dochiri.tooltestlab.order.domain.vo.StreetAddress;
import com.dochiri.tooltestlab.order.domain.vo.ZipCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private static Order order() {
        return Order.create(MemberReferenceId.parse("01900000-0000-7000-8000-000000000001"),
                CartReferenceId.parse("01900000-0000-7000-8000-000000000003"), 1,
                OrderLines.of(OrderLine.of(ProductReferenceId.parse("01900000-0000-7000-8000-000000000002"),
                        ProductName.of("상품"), Quantity.of(2), Money.of(100))),
                ShippingAddress.of(RecipientName.of("홍길동"), Phone.of("010-1234-5678"),
                        StreetAddress.of("서울시 주소"), ZipCode.of("12345")));
    }

    @Test
    @DisplayName("주문 상태 전이는 새 객체를 반환하고 ID와 주문 정보를 유지한다")
    void transitionsImmutably() {
        // given
        final Order awaitingStock = order();
        final Order pending = awaitingStock.confirmStock();

        // when
        final Order paid = pending.pay();
        final Order shipping = paid.startShipping();
        final Order delivered = shipping.deliver();
        final Order cancelled = pending.cancel();

        // then
        assertEquals(OrderStatus.PENDING, pending.status());
        assertEquals(OrderStatus.STOCK_PENDING, awaitingStock.status());
        assertEquals(OrderStatus.REJECTED, awaitingStock.rejectStock().status());
        assertThrows(InvalidOrderStatusException.class, awaitingStock::pay);
        assertEquals(OrderStatus.PAID, paid.status());
        assertEquals(OrderStatus.SHIPPING, shipping.status());
        assertEquals(OrderStatus.DELIVERED, delivered.status());
        assertEquals(OrderStatus.CANCELLED, cancelled.status());
        assertEquals(pending.id(), delivered.id());
        assertEquals(pending.memberId(), delivered.memberId());
        assertEquals(pending.items(), delivered.items());
        assertEquals(pending.shippingAddress(), delivered.shippingAddress());
        assertEquals(Money.of(200), delivered.totalAmount());
        assertThrows(InvalidOrderStatusException.class, pending::startShipping);
        assertThrows(InvalidOrderStatusException.class, paid::pay);
        assertThrows(InvalidOrderStatusException.class, delivered::cancel);
        assertThrows(InvalidOrderStatusException.class, cancelled::cancel);
    }

    @Test
    @DisplayName("주문 복원은 null 필수 필드를 거부한다")
    void rejectsNullFields() {
        // given
        final Order order = order();

        // when
        // then
        assertThrows(NullPointerException.class, () -> Order.restore(null, order.memberId(), order.cartId(), order.cartRevision(), order.items(),
                order.status(), order.shippingAddress(), order.totalAmount()));
        assertThrows(NullPointerException.class, () -> Order.restore(order.id(), null, order.cartId(), order.cartRevision(), order.items(),
                order.status(), order.shippingAddress(), order.totalAmount()));
        assertThrows(NullPointerException.class, () -> Order.restore(order.id(), order.memberId(), order.cartId(), order.cartRevision(), null,
                order.status(), order.shippingAddress(), order.totalAmount()));
        assertThrows(NullPointerException.class, () -> Order.restore(order.id(), order.memberId(), order.cartId(), order.cartRevision(), order.items(),
                null, order.shippingAddress(), order.totalAmount()));
        assertThrows(NullPointerException.class, () -> Order.restore(order.id(), order.memberId(), order.cartId(), order.cartRevision(), order.items(),
                order.status(), null, order.totalAmount()));
        assertThrows(NullPointerException.class, () -> Order.restore(order.id(), order.memberId(), order.cartId(), order.cartRevision(), order.items(),
                order.status(), order.shippingAddress(), null));
    }
}
