package com.dochiri.tooltestlab.review.application.exception;

public final class NotReviewOwnerException extends ReviewApplicationException {

    public NotReviewOwnerException() {
        super("리뷰를 변경할 권한이 없습니다.");
    }

}
