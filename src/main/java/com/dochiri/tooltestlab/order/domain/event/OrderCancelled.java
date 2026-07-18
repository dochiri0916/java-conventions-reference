package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderIdException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException;
import com.dochiri.tooltestlab.order.domain.model.OrderCancelledItems;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;

public record OrderCancelled(
        OrderId orderId,
        OrderCancelledItems items) {

    public OrderCancelled {
        if (orderId == null) {
            throw InvalidOrderIdException.required();
        }
        if (items == null) {
            throw InvalidOrderLinesException.required();
        }
    }

    public record ItemDetail(ProductId productId, Quantity quantity) {
    }
}
