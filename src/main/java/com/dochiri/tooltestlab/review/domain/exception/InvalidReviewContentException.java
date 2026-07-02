package com.dochiri.tooltestlab.review.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidReviewContentException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ReviewDomainErrorCode errorCode;

    private InvalidReviewContentException(final ReviewDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidReviewContentException badRvwContent() {
        return new InvalidReviewContentException(ReviewDomainErrorCode.BAD_RVW_CONTENT);
    }

    public static InvalidReviewContentException required() {
        return new InvalidReviewContentException(ReviewDomainErrorCode.REVIEW_CONTENT_REQUIRED);
    }

    public ReviewDomainErrorCode code() {
        return errorCode;
    }
}
