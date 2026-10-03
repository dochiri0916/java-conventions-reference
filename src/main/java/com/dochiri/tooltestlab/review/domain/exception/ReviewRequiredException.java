package com.dochiri.tooltestlab.review.domain.exception;

public final class ReviewRequiredException extends ReviewDomainException {

    public ReviewRequiredException() {
        super("리뷰 정보는 필수입니다.");
    }

}
