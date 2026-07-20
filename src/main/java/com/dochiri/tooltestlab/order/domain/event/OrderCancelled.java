package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;
import com.dochiri.tooltestlab.order.domain.model.OrderCancelledItems;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;

public record OrderCancelled(
        OrderId orderId,
        OrderCancelledItems items) {

    public OrderCancelled {
        if (orderId == null) {
            throw OrderDomainException.orderIdRequired();
        }
        if (items == null) {
            throw OrderDomainException.orderLinesRequired();
        }
    }

    public record ItemDetail(ProductId productId, Quantity quantity) {
    }
}
