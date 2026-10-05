package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.order.domain.exception.OrderRequiredException;
import com.dochiri.tooltestlab.order.domain.Order;

public record OrderPlaced(Order order) {

    public OrderPlaced {
        if (order == null) {
            throw new OrderRequiredException();
        }
    }

}
