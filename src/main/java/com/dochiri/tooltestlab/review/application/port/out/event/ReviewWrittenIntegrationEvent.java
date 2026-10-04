package com.dochiri.tooltestlab.review.application.port.out.event;

public record ReviewWrittenIntegrationEvent(String reviewId, String memberId, String productId, int rating) implements ReviewIntegrationEvent {
    public static final String TYPE = "review.review-written.v1";

    @Override
    public String eventType() {
        return TYPE;
    }

    @Override
    public String aggregateId() {
        return reviewId;
    }
}
