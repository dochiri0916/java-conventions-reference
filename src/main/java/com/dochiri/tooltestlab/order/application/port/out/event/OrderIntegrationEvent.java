package com.dochiri.tooltestlab.order.application.port.out.event;

import com.dochiri.tooltestlab.global.messaging.IntegrationEvent;

public sealed interface OrderIntegrationEvent extends IntegrationEvent
        permits OrderPlacedIntegrationEvent, OrderConfirmedIntegrationEvent, OrderCancelledIntegrationEvent, OrderRejectedIntegrationEvent {
}
