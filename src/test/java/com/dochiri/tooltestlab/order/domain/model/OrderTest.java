package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    @DisplayName("주문을 생성할 수 있다")
    void createOrder() {
        OrderLine line = new OrderLine("product1", "노트북", 2, 1_000_000L);
        ShippingAddress address = new ShippingAddress("홍길동", "01012345678", "서울시", "12345");
        Order order = Order.create(new MemberId("member1"), OrderLines.from(List.of(line)), address);

        assertThat(order.id().value()).isNotBlank();
        assertThat(order.memberId()).isEqualTo(new MemberId("member1"));
        assertThat(order.items().size()).isEqualTo(1);
        assertThat(order.totalAmount()).isEqualTo(Money.of(2_000_000L));
        assertThat(order.status()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("주문을 취소할 수 있다 (PENDING 상태)")
    void cancelOrder() {
        OrderLine line = new OrderLine("product1", "노트북", 2, 1_000_000L);
        Order order = Order.create(new MemberId("member1"), OrderLines.from(List.of(line)),
                new ShippingAddress("홍길동", "01012345678", "서울시", "12345"));

        Order cancelled = order.cancel();

        assertThat(cancelled.status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("이미 취소된 주문은 다시 취소할 수 없다")
    void cannotCancelTwice() {
        OrderLine line = new OrderLine("product1", "노트북", 2, 1_000_000L);
        Order order = Order.create(new MemberId("member1"), OrderLines.from(List.of(line)),
                new ShippingAddress("홍길동", "01012345678", "서울시", "12345"));
        Order cancelled = order.cancel();

        assertThatThrownBy(cancelled::cancel)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("취소");
    }

    @Test
    @DisplayName("배송 완료된 주문은 취소할 수 없다")
    void cannotCancelDelivered() {
        OrderLine line = new OrderLine("product1", "노트북", 2, 1_000_000L);
        ShippingAddress address = new ShippingAddress("홍길동", "01012345678", "서울시", "12345");
        Order order = Order.create(new MemberId("member1"), OrderLines.from(List.of(line)), address);
        Order paid = order.pay();
        Order shipped = paid.startShipping();
        Order delivered = shipped.deliver();

        assertThatThrownBy(delivered::cancel)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("취소");
    }
}
