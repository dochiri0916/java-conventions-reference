package com.dochiri.tooltestlab.category.domain.exception;

public abstract class CategoryDomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CategoryDomainErrorCode code;

    protected CategoryDomainException(final CategoryDomainErrorCode code, final String message) {
        super(message);
        this.code = code;
    }

    public CategoryDomainErrorCode errorCode() {
        return code;
    }
}
