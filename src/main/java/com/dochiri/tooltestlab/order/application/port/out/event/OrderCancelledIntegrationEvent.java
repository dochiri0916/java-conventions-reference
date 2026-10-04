package com.dochiri.tooltestlab.order.application.port.out.event;

public record OrderCancelledIntegrationEvent(String orderId, String memberId, String cartId, long cartRevision) implements OrderIntegrationEvent {
    public static final String TYPE = "order.order-cancelled.v1";

    @Override
    public String eventType() {
        return TYPE;
    }

    @Override
    public String aggregateId() {
        return orderId;
    }
}
