package com.dochiri.tooltestlab.order.application.port.out.event;

public record OrderPlacedIntegrationEvent(String orderId, String memberId, String cartId, long cartRevision, java.util.List<Item> items) implements OrderIntegrationEvent {
    public static final String TYPE = "order.order-placed.v1";

    @Override
    public String eventType() {
        return TYPE;
    }

    @Override
    public String aggregateId() {
        return orderId;
    }

    public OrderPlacedIntegrationEvent {
        items = java.util.List.copyOf(items);
    }

    public record Item(String productId, String productName, int quantity, long unitPrice) {
    }
}
