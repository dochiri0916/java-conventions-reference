package com.dochiri.tooltestlab.review.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidRatingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ReviewDomainErrorCode errorCode;
    private final int ratingField;

    private InvalidRatingException(final ReviewDomainErrorCode errorCode, final int rating) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.ratingField = rating;
    }

    public static InvalidRatingException invalidRating(final int rating) {
        return new InvalidRatingException(ReviewDomainErrorCode.INVALID_RATING, rating);
    }

    public ReviewDomainErrorCode code() {
        return errorCode;
    }

    public int rating() {
        return ratingField;
    }
}
