package com.dochiri.tooltestlab.review.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidReviewException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ReviewDomainErrorCode errorCode;

    private InvalidReviewException(final ReviewDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidReviewException invalidReview() {
        return new InvalidReviewException(ReviewDomainErrorCode.INVALID_REVIEW);
    }

    public static InvalidReviewException required() {
        return new InvalidReviewException(ReviewDomainErrorCode.REVIEW_REQUIRED);
    }

    public ReviewDomainErrorCode code() {
        return errorCode;
    }
}
