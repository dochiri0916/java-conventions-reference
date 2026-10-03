package com.dochiri.tooltestlab.order.domain;

import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderStatusException;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.OrderId;
import com.dochiri.tooltestlab.order.domain.vo.OrderLines;
import com.dochiri.tooltestlab.order.domain.vo.ShippingAddress;

import static java.util.Objects.requireNonNull;

public final class Order {

    private final OrderId id;
    private final MemberReferenceId memberId;
    private final OrderLines items;
    private final OrderStatus status;
    private final ShippingAddress shippingAddress;
    private final Money totalAmount;

    private Order(
            final OrderId id,
            final MemberReferenceId memberId,
            final OrderLines items,
            final OrderStatus status,
            final ShippingAddress shippingAddress,
            final Money totalAmount
    ) {
        this.id = requireNonNull(id);
        this.memberId = requireNonNull(memberId);
        this.items = requireNonNull(items);
        this.status = requireNonNull(status);
        this.shippingAddress = requireNonNull(shippingAddress);
        this.totalAmount = requireNonNull(totalAmount);
    }

    public static Order create(
            final MemberReferenceId memberId,
            final OrderLines items,
            final ShippingAddress shippingAddress
    ) {
        return new Order(
                OrderId.generate(), memberId, items, OrderStatus.PENDING, shippingAddress, items.totalAmount());
    }

    public static Order restore(
            final OrderId id,
            final MemberReferenceId memberId,
            final OrderLines items,
            final OrderStatus status,
            final ShippingAddress shippingAddress,
            final Money totalAmount
    ) {
        return new Order(id, memberId, items, status, shippingAddress, totalAmount);
    }

    public Order pay() {
        if (status != OrderStatus.PENDING) {
            throw new InvalidOrderStatusException();
        }
        return restore(id, memberId, items, OrderStatus.PAID, shippingAddress, totalAmount);
    }

    public Order startShipping() {
        if (status != OrderStatus.PAID) {
            throw new InvalidOrderStatusException();
        }
        return restore(id, memberId, items, OrderStatus.SHIPPING, shippingAddress, totalAmount);
    }

    public Order deliver() {
        if (status != OrderStatus.SHIPPING) {
            throw new InvalidOrderStatusException();
        }
        return restore(id, memberId, items, OrderStatus.DELIVERED, shippingAddress, totalAmount);
    }

    public Order cancel() {
        if (status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {
            throw new InvalidOrderStatusException();
        }
        return restore(id, memberId, items, OrderStatus.CANCELLED, shippingAddress, totalAmount);
    }

    public OrderId id() {
        return id;
    }

    public MemberReferenceId memberId() {
        return memberId;
    }

    public OrderLines items() {
        return items;
    }

    public OrderStatus status() {
        return status;
    }

    public ShippingAddress shippingAddress() {
        return shippingAddress;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof Order order && id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
