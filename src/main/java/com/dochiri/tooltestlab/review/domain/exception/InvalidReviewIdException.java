package com.dochiri.tooltestlab.review.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidReviewIdException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ReviewDomainErrorCode errorCode;

    private InvalidReviewIdException(final ReviewDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidReviewIdException invalidReviewId() {
        return new InvalidReviewIdException(ReviewDomainErrorCode.INVALID_REVIEW_ID);
    }

    public static InvalidReviewIdException required() {
        return new InvalidReviewIdException(ReviewDomainErrorCode.REVIEW_ID_REQUIRED);
    }

    public ReviewDomainErrorCode code() {
        return errorCode;
    }
}
