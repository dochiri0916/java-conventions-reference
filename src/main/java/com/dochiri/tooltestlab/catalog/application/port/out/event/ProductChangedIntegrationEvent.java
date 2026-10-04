package com.dochiri.tooltestlab.catalog.application.port.out.event;

public record ProductChangedIntegrationEvent(String productId, String name, long priceAmount, int stock, String status) implements CatalogIntegrationEvent {
    public static final String TYPE = "catalog.product-changed.v1";

    @Override
    public String eventType() {
        return TYPE;
    }

    @Override
    public String aggregateId() {
        return productId;
    }
}
