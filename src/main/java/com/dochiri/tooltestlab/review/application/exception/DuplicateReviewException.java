package com.dochiri.tooltestlab.review.application.exception;

public final class DuplicateReviewException extends ReviewApplicationException {

    public DuplicateReviewException() {
        super("이미 리뷰를 작성했습니다.");
    }

}
