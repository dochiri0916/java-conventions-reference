package com.dochiri.tooltestlab.review.application.exception;

import static java.util.Objects.requireNonNull;

public final class DuplicateReviewException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ReviewApplicationErrorCode errorCode;

    private DuplicateReviewException(final ReviewApplicationErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static DuplicateReviewException duplicateReview() {
        return new DuplicateReviewException(ReviewApplicationErrorCode.DUPLICATE_REVIEW);
    }

    public ReviewApplicationErrorCode code() {
        return errorCode;
    }
}
