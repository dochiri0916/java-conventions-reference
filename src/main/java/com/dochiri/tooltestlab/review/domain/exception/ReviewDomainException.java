package com.dochiri.tooltestlab.review.domain.exception;

public abstract class ReviewDomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ReviewDomainErrorCode code;

    protected ReviewDomainException(final ReviewDomainErrorCode code, final String message) {
        super(message);
        this.code = code;
    }

    public ReviewDomainErrorCode errorCode() {
        return code;
    }
}
