package com.dochiri.tooltestlab.review.application.exception;

import static java.util.Objects.requireNonNull;

public final class ReviewNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ReviewApplicationErrorCode errorCode;
    private final String reviewPublicId;

    private ReviewNotFoundException(final ReviewApplicationErrorCode errorCode, final String reviewPublicId) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.reviewPublicId = requireNonNull(reviewPublicId);
    }

    public static ReviewNotFoundException reviewNotFound(final String reviewPublicId) {
        return new ReviewNotFoundException(ReviewApplicationErrorCode.REVIEW_NOT_FOUND, reviewPublicId);
    }

    public ReviewApplicationErrorCode code() {
        return errorCode;
    }

    public String reviewPublicId() {
        return reviewPublicId;
    }
}
