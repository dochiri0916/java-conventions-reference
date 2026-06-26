package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.member.domain.model.MemberId;

import static java.util.Objects.requireNonNull;

/**
 * 주문 Aggregate Root.
 */
public record Order(
        OrderId id,
        MemberId memberId,
        OrderLines items,
        OrderStatus status,
        ShippingAddress shippingAddress,
        Money totalAmount
) {

    public Order {
        requireNonNull(id, "주문 ID는 null일 수 없습니다.");
        requireNonNull(memberId, "회원 ID는 null일 수 없습니다.");
        requireNonNull(items, "주문 상품 목록은 null일 수 없습니다.");
        requireNonNull(status, "주문 상태는 null일 수 없습니다.");
        requireNonNull(shippingAddress, "배송지는 null일 수 없습니다.");
        requireNonNull(totalAmount, "총 금액은 null일 수 없습니다.");
    }

    public static Order create(MemberId memberId, OrderLines items, ShippingAddress shippingAddress) {
        return new Order(
                OrderId.generate(),
                memberId,
                items,
                OrderStatus.PENDING,
                shippingAddress,
                items.totalAmount());
    }

    /**
     * 결제 완료.
     */
    public Order pay() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("주문 접수 상태에서만 결제할 수 있습니다. 현재 상태: " + status);
        }
        return new Order(id, memberId, items, OrderStatus.PAID, shippingAddress, totalAmount);
    }

    /**
     * 배송 시작.
     */
    public Order startShipping() {
        if (status != OrderStatus.PAID) {
            throw new IllegalStateException("결제 완료 상태에서만 배송할 수 있습니다. 현재 상태: " + status);
        }
        return new Order(id, memberId, items, OrderStatus.SHIPPING, shippingAddress, totalAmount);
    }

    /**
     * 배송 완료.
     */
    public Order deliver() {
        if (status != OrderStatus.SHIPPING) {
            throw new IllegalStateException("배송 중 상태에서만 배송 완료할 수 있습니다. 현재 상태: " + status);
        }
        return new Order(id, memberId, items, OrderStatus.DELIVERED, shippingAddress, totalAmount);
    }

    /**
     * 주문 취소.
     */
    public Order cancel() {
        if (status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("이미 " + status + " 상태인 주문은 취소할 수 없습니다.");
        }
        return new Order(id, memberId, items, OrderStatus.CANCELLED, shippingAddress, totalAmount);
    }
}
