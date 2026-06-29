package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.review.domain.exception.InvalidReviewIdException;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record ReviewId(String value) {

    public ReviewId {
        value = requireNonNull(value).strip();
        if (value.isBlank()) {
            throw new InvalidReviewIdException();
        }
    }

    public static ReviewId generate() {
        return new ReviewId(UUID.randomUUID().toString().replace("-", ""));
    }
}
