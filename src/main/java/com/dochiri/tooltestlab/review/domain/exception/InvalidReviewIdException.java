package com.dochiri.tooltestlab.review.domain.exception;

public final class InvalidReviewIdException extends ReviewDomainException {

    public InvalidReviewIdException() {
        super("유효하지 않은 리뷰 ID입니다.");
    }

}
