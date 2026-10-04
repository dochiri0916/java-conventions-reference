package com.dochiri.tooltestlab.order.application.port.out.event;

public record OrderConfirmedIntegrationEvent(String orderId, String memberId, String cartId, long cartRevision) implements OrderIntegrationEvent {
    public static final String TYPE = "order.order-confirmed.v1";

    @Override
    public String eventType() {
        return TYPE;
    }

    @Override
    public String aggregateId() {
        return orderId;
    }
}
