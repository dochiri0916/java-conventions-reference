package com.dochiri.tooltestlab.product.application.exception;

public abstract class ProductApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductErrorCode errorCode;

    protected ProductApplicationException(final ProductErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ProductErrorCode errorCode() {
        return errorCode;
    }
}
