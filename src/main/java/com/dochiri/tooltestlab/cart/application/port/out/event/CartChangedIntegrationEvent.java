package com.dochiri.tooltestlab.cart.application.port.out.event;

public record CartChangedIntegrationEvent(String cartId, String memberId, long revision, java.util.List<Item> items) implements CartIntegrationEvent {
    public static final String TYPE = "cart.cart-changed.v1";

    @Override
    public String eventType() {
        return TYPE;
    }

    @Override
    public String aggregateId() {
        return cartId;
    }

    public CartChangedIntegrationEvent {
        items = java.util.List.copyOf(items);
    }

    public record Item(String productId, String productName, int quantity, long unitPrice) {
    }
}
