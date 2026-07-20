package com.dochiri.tooltestlab.review.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class ReviewDomainException extends DomainException {

    private ReviewDomainException(final ReviewDomainErrorCode errorCode, final Map<String, ?> extensions) {
        super(errorCode, errorCode.detail(), extensions);
    }

    public static ReviewDomainException reviewIdRequired() {
        return of(ReviewDomainErrorCode.REVIEW_ID_REQUIRED);
    }

    public static ReviewDomainException invalidReviewId() {
        return of(ReviewDomainErrorCode.INVALID_REVIEW_ID);
    }

    public static ReviewDomainException invalidRating(final int rating) {
        return new ReviewDomainException(
                ReviewDomainErrorCode.INVALID_RATING,
                Map.of("rating", rating)
        );
    }

    public static ReviewDomainException ratingRequired() {
        return of(ReviewDomainErrorCode.RATING_REQUIRED);
    }

    public static ReviewDomainException badReviewContent() {
        return of(ReviewDomainErrorCode.BAD_RVW_CONTENT);
    }

    public static ReviewDomainException reviewContentRequired() {
        return of(ReviewDomainErrorCode.REVIEW_CONTENT_REQUIRED);
    }

    public static ReviewDomainException reviewRequired() {
        return of(ReviewDomainErrorCode.REVIEW_REQUIRED);
    }

    private static ReviewDomainException of(final ReviewDomainErrorCode errorCode) {
        return new ReviewDomainException(errorCode, Map.of());
    }
}
