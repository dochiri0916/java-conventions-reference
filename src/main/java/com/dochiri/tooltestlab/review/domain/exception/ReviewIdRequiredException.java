package com.dochiri.tooltestlab.review.domain.exception;

public final class ReviewIdRequiredException extends ReviewDomainException {

    public ReviewIdRequiredException() {
        super("리뷰 ID는 필수입니다.");
    }

}
