package com.dochiri.tooltestlab.review.application.exception;

public abstract class ReviewApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ReviewApplicationErrorCode errorCode;

    protected ReviewApplicationException(final ReviewApplicationErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ReviewApplicationErrorCode errorCode() {
        return errorCode;
    }
}
