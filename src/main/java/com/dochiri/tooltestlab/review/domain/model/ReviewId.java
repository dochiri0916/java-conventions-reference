package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.review.domain.exception.ReviewDomainException;

import java.util.UUID;

public record ReviewId(String value) {

    public ReviewId {
        if (value == null) {
            throw ReviewDomainException.reviewIdRequired();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw ReviewDomainException.invalidReviewId();
        }
    }

    public static ReviewId generate() {
        return new ReviewId(UUID.randomUUID().toString().replace("-", ""));
    }
}
