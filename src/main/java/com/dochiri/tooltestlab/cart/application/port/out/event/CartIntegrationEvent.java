package com.dochiri.tooltestlab.cart.application.port.out.event;

import com.dochiri.tooltestlab.global.messaging.IntegrationEvent;

public sealed interface CartIntegrationEvent extends IntegrationEvent
        permits CartChangedIntegrationEvent {
}
