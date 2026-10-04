package com.dochiri.tooltestlab.catalog.application.port.out.event;

public record StockReleasedIntegrationEvent(String orderId) implements CatalogIntegrationEvent {
    public static final String TYPE = "catalog.stock-released.v1";

    @Override
    public String eventType() {
        return TYPE;
    }

    @Override
    public String aggregateId() {
        return orderId;
    }
}
