package com.dochiri.tooltestlab.catalog.category.application.exception;

public abstract sealed class CategoryApplicationException extends RuntimeException
        permits DuplicateCategoryNameException,
        InvalidCommandException {
    protected CategoryApplicationException(final String message) {
        super(message);
    }

    protected CategoryApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
