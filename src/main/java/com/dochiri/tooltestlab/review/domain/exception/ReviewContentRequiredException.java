package com.dochiri.tooltestlab.review.domain.exception;

public final class ReviewContentRequiredException extends ReviewDomainException {

    public ReviewContentRequiredException() {
        super("리뷰 내용은 필수입니다.");
    }

}
