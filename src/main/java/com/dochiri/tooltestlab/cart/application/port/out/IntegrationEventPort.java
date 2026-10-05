package com.dochiri.tooltestlab.cart.application.port.out;

import com.dochiri.tooltestlab.cart.application.port.out.event.CartIntegrationEvent;

@FunctionalInterface
public interface IntegrationEventPort {
    void publish(CartIntegrationEvent event);
}
