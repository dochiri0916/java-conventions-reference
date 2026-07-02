package com.dochiri.tooltestlab.review.application.exception;

import static java.util.Objects.requireNonNull;

public final class ReviewNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ReviewApplicationErrorCode errorCode;
    private final String missingReviewId;

    private ReviewNotFoundException(final ReviewApplicationErrorCode errorCode, final String missingReviewId) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.missingReviewId = requireNonNull(missingReviewId);
    }

    public static ReviewNotFoundException reviewNotFound(final String reviewId) {
        return new ReviewNotFoundException(ReviewApplicationErrorCode.REVIEW_NOT_FOUND, reviewId);
    }

    public ReviewApplicationErrorCode code() {
        return errorCode;
    }

    public String reviewId() {
        return missingReviewId;
    }
}
