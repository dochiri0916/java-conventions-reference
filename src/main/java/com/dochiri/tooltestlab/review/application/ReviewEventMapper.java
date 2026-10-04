package com.dochiri.tooltestlab.review.application;

import com.dochiri.tooltestlab.review.application.port.out.event.*;
import com.dochiri.tooltestlab.review.domain.event.*;

public final class ReviewEventMapper {
    private ReviewEventMapper() {
    }

    public static ReviewWrittenIntegrationEvent toIntegration(final ReviewWritten event) {
        final var review = event.review();
        return new ReviewWrittenIntegrationEvent(review.id().value(), review.memberId().value(),
                review.productId().value(), review.rating().value());
    }

    public static ReviewDeletedIntegrationEvent toIntegration(final ReviewDeleted event) {
        return new ReviewDeletedIntegrationEvent(event.reviewId().value(), event.productId().value());
    }
}
