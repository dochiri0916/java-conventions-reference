package com.dochiri.tooltestlab.order.application.port.out.event;

public record OrderRejectedIntegrationEvent(String orderId, String reason) implements OrderIntegrationEvent {
    public static final String TYPE = "order.order-rejected.v1";

    @Override
    public String eventType() {
        return TYPE;
    }

    @Override
    public String aggregateId() {
        return orderId;
    }
}
