package com.dochiri.tooltestlab.order.application.port.in;

import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;

@FunctionalInterface
public interface HandleOrderPlacedUseCase {

    void handleOrderPlaced(OrderPlaced event);
}
