package com.dochiri.tooltestlab.review.domain.exception;

public final class InvalidRatingException extends ReviewDomainException {

    public InvalidRatingException() {
        super("평점은 1에서 5 사이여야 합니다.");
    }

}
