package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderStatusException;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.product.domain.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    @DisplayName("주문을 생성할 수 있다")
    void createOrder() {
        // given
        final OrderLine line = new OrderLine(new ProductId("product1"), new ProductName("노트북"),
                new Quantity(2), Money.of(1_000_000L));
        final ShippingAddress address = new ShippingAddress(
                new RecipientName("홍길동"),
                new Phone("01012345678"),
                new StreetAddress("서울시"),
                new ZipCode("12345"));

        // when
        final Order order = Order.create(new MemberId("member1"), OrderLines.from(List.of(line)), address);

        // then
        assertThat(order.id().value()).isNotBlank();
        assertThat(order.memberId()).isEqualTo(new MemberId("member1"));
        assertThat(order.items().size()).isEqualTo(1);
        assertThat(order.totalAmount()).isEqualTo(Money.of(2_000_000L));
        assertThat(order.status()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("주문을 취소할 수 있다 (PENDING 상태)")
    void cancelOrder() {
        // given
        final OrderLine line = new OrderLine(new ProductId("product1"), new ProductName("노트북"),
                new Quantity(2), Money.of(1_000_000L));
        final ShippingAddress address = new ShippingAddress(
                new RecipientName("홍길동"),
                new Phone("01012345678"),
                new StreetAddress("서울시"),
                new ZipCode("12345"));
        final Order order = Order.create(new MemberId("member1"), OrderLines.from(List.of(line)), address);

        // when
        final Order cancelled = order.cancel();

        // then
        assertThat(cancelled.status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("이미 취소된 주문은 다시 취소할 수 없다")
    void cannotCancelTwice() {
        // given
        final OrderLine line = new OrderLine(new ProductId("product1"), new ProductName("노트북"),
                new Quantity(2), Money.of(1_000_000L));
        final ShippingAddress address = new ShippingAddress(
                new RecipientName("홍길동"),
                new Phone("01012345678"),
                new StreetAddress("서울시"),
                new ZipCode("12345"));
        final Order order = Order.create(new MemberId("member1"), OrderLines.from(List.of(line)), address);
        final Order cancelled = order.cancel();

        // when & then
        assertThatThrownBy(cancelled::cancel)
                .isInstanceOf(InvalidOrderStatusException.class);
    }

    @Test
    @DisplayName("배송 완료된 주문은 취소할 수 없다")
    void cannotCancelDelivered() {
        // given
        final OrderLine line = new OrderLine(new ProductId("product1"), new ProductName("노트북"),
                new Quantity(2), Money.of(1_000_000L));
        final ShippingAddress address = new ShippingAddress(
                new RecipientName("홍길동"),
                new Phone("01012345678"),
                new StreetAddress("서울시"),
                new ZipCode("12345"));
        final Order order = Order.create(new MemberId("member1"), OrderLines.from(List.of(line)), address);
        final Order paid = order.pay();
        final Order shipped = paid.startShipping();
        final Order delivered = shipped.deliver();

        // when & then
        assertThatThrownBy(delivered::cancel)
                .isInstanceOf(InvalidOrderStatusException.class);
    }
}
