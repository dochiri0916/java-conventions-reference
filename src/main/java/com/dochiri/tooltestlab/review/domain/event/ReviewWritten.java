package com.dochiri.tooltestlab.review.domain.event;

import com.dochiri.tooltestlab.review.domain.exception.ReviewRequiredException;
import com.dochiri.tooltestlab.review.domain.Review;

public record ReviewWritten(Review review) {

    public ReviewWritten {
        if (review == null) {
            throw new ReviewRequiredException();
        }
    }

}
