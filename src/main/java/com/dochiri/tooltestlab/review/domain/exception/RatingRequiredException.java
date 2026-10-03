package com.dochiri.tooltestlab.review.domain.exception;

public final class RatingRequiredException extends ReviewDomainException {

    public RatingRequiredException() {
        super("평점은 필수입니다.");
    }

}
