package com.dochiri.tooltestlab.catalog.product.application.port.out;

import com.dochiri.tooltestlab.catalog.product.application.port.out.event.CatalogIntegrationEvent;

@FunctionalInterface
public interface IntegrationEventPort {
    void publish(CatalogIntegrationEvent event);
}
