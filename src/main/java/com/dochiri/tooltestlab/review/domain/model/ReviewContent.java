package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.review.domain.exception.InvalidReviewContentException;

import static java.util.Objects.requireNonNull;

public record ReviewContent(String value) {

    public ReviewContent {
        requireNonNull(value, "리뷰 내용은 null일 수 없습니다.");
        value = value.strip();
        if (value.isBlank()) {
            throw InvalidReviewContentException.badRvwContent();
        }
    }
}
