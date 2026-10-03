package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.review.domain.exception.InvalidReviewIdException;
import com.dochiri.tooltestlab.review.domain.exception.ReviewIdRequiredException;

import java.util.UUID;

public record ReviewId(String value) {

    public ReviewId {
        if (value == null) {
            throw new ReviewIdRequiredException();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw new InvalidReviewIdException();
        }
    }

    public static ReviewId generate() {
        return new ReviewId(UUID.randomUUID().toString().replace("-", ""));
    }
}
