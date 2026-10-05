package com.dochiri.tooltestlab.order.application.port.out;

import com.dochiri.tooltestlab.order.application.port.out.event.OrderIntegrationEvent;

@FunctionalInterface
public interface IntegrationEventPort {
    void publish(OrderIntegrationEvent event);
}
