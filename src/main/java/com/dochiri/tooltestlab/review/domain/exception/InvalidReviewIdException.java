package com.dochiri.tooltestlab.review.domain.exception;

public final class InvalidReviewIdException extends ReviewDomainException {

    public InvalidReviewIdException() {
        super(ReviewDomainErrorCode.INVALID_REVIEW_ID, "리뷰 ID는 공백일 수 없습니다.");
    }
}
