package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.review.domain.exception.ReviewDomainException;

public record ReviewContent(String value) {

    public ReviewContent {
        if (value == null) {
            throw ReviewDomainException.reviewContentRequired();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw ReviewDomainException.badReviewContent();
        }
    }
}
