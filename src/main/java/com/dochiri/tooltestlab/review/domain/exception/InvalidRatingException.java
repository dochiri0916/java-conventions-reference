package com.dochiri.tooltestlab.review.domain.exception;

public final class InvalidRatingException extends ReviewDomainException {

    public InvalidRatingException(final int rating) {
        super(ReviewDomainErrorCode.INVALID_RATING, "평점은 1~5 사이여야 합니다. 입력값: " + rating);
    }
}
