package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.order.domain.exception.OrderRequiredException;
import com.dochiri.tooltestlab.order.domain.Order;

public record OrderCancelled(Order order) {

    public OrderCancelled {
        if (order == null) {
            throw new OrderRequiredException();
        }
    }

}
