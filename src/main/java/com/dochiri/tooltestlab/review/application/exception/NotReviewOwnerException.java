package com.dochiri.tooltestlab.review.application.exception;

import static java.util.Objects.requireNonNull;

public final class NotReviewOwnerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ReviewApplicationErrorCode errorCode;

    private NotReviewOwnerException(final ReviewApplicationErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static NotReviewOwnerException notReviewOwner() {
        return new NotReviewOwnerException(ReviewApplicationErrorCode.NOT_REVIEW_OWNER);
    }

    public ReviewApplicationErrorCode code() {
        return errorCode;
    }
}
