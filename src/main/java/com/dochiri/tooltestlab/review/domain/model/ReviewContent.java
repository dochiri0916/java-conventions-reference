package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.review.domain.exception.InvalidReviewContentException;

public record ReviewContent(String value) {

    public ReviewContent {
        if (value == null) {
            throw InvalidReviewContentException.required();
        }
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidReviewContentException.badRvwContent();
        }
    }
}
