package com.dochiri.tooltestlab.review.domain.exception;

public final class InvalidReviewContentException extends ReviewDomainException {

    public InvalidReviewContentException() {
        super(ReviewDomainErrorCode.BAD_RVW_CONTENT, "리뷰 내용은 빈 값일 수 없습니다.");
    }
}
