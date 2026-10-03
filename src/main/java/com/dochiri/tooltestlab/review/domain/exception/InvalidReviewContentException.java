package com.dochiri.tooltestlab.review.domain.exception;

public final class InvalidReviewContentException extends ReviewDomainException {

    public InvalidReviewContentException() {
        super("리뷰 내용이 올바르지 않습니다.");
    }

}
