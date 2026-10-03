package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.review.domain.exception.InvalidReviewContentException;
import com.dochiri.tooltestlab.review.domain.exception.ReviewContentRequiredException;

public record ReviewContent(String value) {

    public ReviewContent {
        if (value == null) {
            throw new ReviewContentRequiredException();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw new InvalidReviewContentException();
        }
    }
}
