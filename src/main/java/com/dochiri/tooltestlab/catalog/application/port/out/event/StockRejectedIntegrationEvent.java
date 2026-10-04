package com.dochiri.tooltestlab.catalog.application.port.out.event;

public record StockRejectedIntegrationEvent(String orderId, String reason) implements CatalogIntegrationEvent {
    public static final String TYPE = "catalog.stock-rejected.v1";

    @Override
    public String eventType() {
        return TYPE;
    }

    @Override
    public String aggregateId() {
        return orderId;
    }
}
