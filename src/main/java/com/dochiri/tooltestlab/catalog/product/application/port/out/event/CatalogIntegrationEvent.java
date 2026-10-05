package com.dochiri.tooltestlab.catalog.product.application.port.out.event;

import com.dochiri.tooltestlab.global.messaging.IntegrationEvent;

public sealed interface CatalogIntegrationEvent extends IntegrationEvent
        permits ProductChangedIntegrationEvent {
}
