package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.review.domain.exception.InvalidReviewIdException;

import java.util.UUID;

public record ReviewId(String value) {

    public ReviewId {
        if (value == null) {
            throw InvalidReviewIdException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidReviewIdException.invalidReviewId();
        }
    }

    public static ReviewId generate() {
        return new ReviewId(UUID.randomUUID().toString().replace("-", ""));
    }
}
