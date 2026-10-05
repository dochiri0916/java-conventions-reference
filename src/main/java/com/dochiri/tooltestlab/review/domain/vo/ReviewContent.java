package com.dochiri.tooltestlab.review.domain.vo;

import com.dochiri.tooltestlab.review.domain.exception.InvalidReviewContentException;
import com.dochiri.tooltestlab.review.domain.exception.ReviewContentRequiredException;

public final class ReviewContent {

    private final String value;

    private ReviewContent(final String value) {
        if (value == null) {
            throw new ReviewContentRequiredException();
        }
        final String normalized = value.strip();
        if (normalized.isBlank()) {
            throw new InvalidReviewContentException();
        }
        this.value = normalized;
    }

    public static ReviewContent of(final String value) {
        return new ReviewContent(value);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final ReviewContent other)) {
            return false;
        }
        return value.equals(
                other.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "ReviewContent[value=" + value + "]";
    }

}
