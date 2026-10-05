package com.dochiri.tooltestlab.review.application.exception;

public abstract sealed class ReviewApplicationException extends RuntimeException
        permits DuplicateReviewException,
        ReviewNotFoundException,
        InvalidCommandException {
    protected ReviewApplicationException(final String message) {
        super(message);
    }

    protected ReviewApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
