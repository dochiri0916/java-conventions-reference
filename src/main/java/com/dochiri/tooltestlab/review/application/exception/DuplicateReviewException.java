package com.dochiri.tooltestlab.review.application.exception;

public final class DuplicateReviewException extends ReviewApplicationException {

    public DuplicateReviewException() {
        super(ReviewApplicationErrorCode.DUPLICATE_REVIEW, "이미 이 상품에 리뷰를 작성하셨습니다.");
    }
}
