package com.dochiri.tooltestlab.review.application.exception;

public abstract sealed class ReviewApplicationException extends RuntimeException
        permits DuplicateReviewException,
        ReviewNotFoundException,
        NotReviewOwnerException,
        InvalidCommandException {
    protected ReviewApplicationException(final String message) {
        super(message);
    }

}
