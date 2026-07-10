package com.dochiri.tooltestlab.review.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidRatingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ReviewDomainErrorCode errorCode;
    private final int invalidRatingValue;

    private InvalidRatingException(final ReviewDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.invalidRatingValue = 0;
    }

    private InvalidRatingException(final ReviewDomainErrorCode errorCode, final int rating) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.invalidRatingValue = rating;
    }

    public static InvalidRatingException invalidRating(final int rating) {
        return new InvalidRatingException(ReviewDomainErrorCode.INVALID_RATING, rating);
    }

    public static InvalidRatingException required() {
        return new InvalidRatingException(ReviewDomainErrorCode.RATING_REQUIRED);
    }

    public ReviewDomainErrorCode code() {
        return errorCode;
    }

    public int rating() {
        return invalidRatingValue;
    }
}
