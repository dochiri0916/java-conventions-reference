package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderStatusException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidShippingAddressException;
import com.dochiri.tooltestlab.catalog.product.domain.model.Money;

public record Order(
        OrderId id,
        MemberId memberId,
        OrderLines items,
        OrderStatus status,
        ShippingAddress shippingAddress,
        Money totalAmount
) {

    public Order {
        if (id == null) {
            throw InvalidOrderIdException.required();
        }
        if (memberId == null) {
            throw com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException.required();
        }
        if (items == null) {
            throw InvalidOrderLinesException.required();
        }
        if (status == null) {
            throw InvalidOrderStatusException.required();
        }
        if (shippingAddress == null) {
            throw InvalidShippingAddressException.required();
        }
        if (totalAmount == null) {
            throw InvalidMoneyException.required();
        }
    }

    public static Order create(final MemberId memberId, final OrderLines items, final ShippingAddress shippingAddress) {
        return new Order(
                OrderId.generate(),
                memberId,
                items,
                OrderStatus.PENDING,
                shippingAddress,
                items.totalAmount());
    }

    public Order pay() {
        if (status != OrderStatus.PENDING) {
            throw InvalidOrderStatusException.badOrderStatus(id, OrderStatus.PENDING.toString(), status.toString());
        }
        return new Order(id, memberId, items, OrderStatus.PAID, shippingAddress, totalAmount);
    }

    public Order startShipping() {
        if (status != OrderStatus.PAID) {
            throw InvalidOrderStatusException.badOrderStatus(id, OrderStatus.PAID.toString(), status.toString());
        }
        return new Order(id, memberId, items, OrderStatus.SHIPPING, shippingAddress, totalAmount);
    }

    public Order deliver() {
        if (status != OrderStatus.SHIPPING) {
            throw InvalidOrderStatusException.badOrderStatus(id, OrderStatus.SHIPPING.toString(), status.toString());
        }
        return new Order(id, memberId, items, OrderStatus.DELIVERED, shippingAddress, totalAmount);
    }

    public Order cancel() {
        if (status == OrderStatus.DELIVERED || status == OrderStatus.CANCELLED) {
            throw InvalidOrderStatusException.badOrderStatus(
                    id, "PENDING / PAID / SHIPPING", status.toString());
        }
        return new Order(id, memberId, items, OrderStatus.CANCELLED, shippingAddress, totalAmount);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other
                || other instanceof Order order && id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
