package com.dochiri.tooltestlab.review.application.port.out.event;

public record ReviewDeletedIntegrationEvent(String reviewId, String productId) implements ReviewIntegrationEvent {
    public static final String TYPE = "review.review-deleted.v1";

    @Override
    public String eventType() {
        return TYPE;
    }

    @Override
    public String aggregateId() {
        return reviewId;
    }
}
