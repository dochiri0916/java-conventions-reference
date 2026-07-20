package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;

import static java.util.Objects.requireNonNull;

public final class Order {

    private final OrderId id;
    private final MemberId memberId;
    private final OrderLines items;
    private final OrderStatus status;
    private final ShippingAddress shippingAddress;
    private final Money totalAmount;

    private Order(
            final OrderId id,
            final MemberId memberId,
            final OrderLines items,
            final OrderStatus status,
            final ShippingAddress shippingAddress,
            final Money totalAmount
    ) {
        this.id = requireNonNull(id, "주문 ID는 null일 수 없습니다.");
        this.memberId = requireNonNull(memberId, "회원 ID는 null일 수 없습니다.");
        this.items = requireNonNull(items, "주문 상품 목록은 null일 수 없습니다.");
        this.status = requireNonNull(status, "주문 상태는 null일 수 없습니다.");
        this.shippingAddress = requireNonNull(shippingAddress, "배송지는 null일 수 없습니다.");
        this.totalAmount = requireNonNull(totalAmount, "총 금액은 null일 수 없습니다.");
    }

    public static Order create(
            final MemberId memberId,
            final OrderLines items,
            final ShippingAddress shippingAddress
    ) {
        return new Order(
                OrderId.generate(), memberId, items, OrderStatus.PENDING, shippingAddress, items.totalAmount());
    }

    public static Order restore(
            final OrderId id,
            final MemberId memberId,
            final OrderLines items,
            final OrderStatus status,
            final ShippingAddress shippingAddress,
            final Money totalAmount
    ) {
        return new Order(id, memberId, items, status, shippingAddress, totalAmount);
    }

    public Order pay() {
        if (status != OrderStatus.PENDING) {
            throw OrderDomainException.badOrderStatus(id, OrderStatus.PENDING.toString(), status.toString());
        }
        return restore(id, memberId, items, OrderStatus.PAID, shippingAddress, totalAmount);
    }

    public Order startShipping() {
        if (status != OrderStatus.PAID) {
            throw OrderDomainException.badOrderStatus(id, OrderStatus.PAID.toString(), status.toString());
        }
        return restore(id, memberId, items, OrderStatus.SHIPPING, shippingAddress, totalAmount);
    }

    public Order deliver() {
        if (status != OrderStatus.SHIPPING) {
            throw OrderDomainException.badOrderStatus(id, OrderStatus.SHIPPING.toString(), status.toString());
        }
        return restore(id, memberId, items, OrderStatus.DELIVERED, shippingAddress, totalAmount);
    }

    public Order cancel() {
        if (status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {
            throw OrderDomainException.badOrderStatus(id, "PENDING / PAID / SHIPPING", status.toString());
        }
        return restore(id, memberId, items, OrderStatus.CANCELLED, shippingAddress, totalAmount);
    }

    public OrderId id() { return id; }
    public MemberId memberId() { return memberId; }
    public OrderLines items() { return items; }
    public OrderStatus status() { return status; }
    public ShippingAddress shippingAddress() { return shippingAddress; }
    public Money totalAmount() { return totalAmount; }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof Order order && id.equals(order.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
