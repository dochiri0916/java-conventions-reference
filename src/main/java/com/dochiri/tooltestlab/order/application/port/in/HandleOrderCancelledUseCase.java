package com.dochiri.tooltestlab.order.application.port.in;

import com.dochiri.tooltestlab.order.domain.event.OrderCancelled;

@FunctionalInterface
public interface HandleOrderCancelledUseCase {

    void handleOrderCancelled(OrderCancelled event);
}
