package com.dochiri.tooltestlab.review.application.exception;

public final class ReviewNotFoundException extends ReviewApplicationException {

    public ReviewNotFoundException() {
        super("리뷰를 찾을 수 없습니다.");
    }

}
