package com.dochiri.tooltestlab.cart.application.exception;

public abstract class CartApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CartErrorCode errorCode;

    protected CartApplicationException(final CartErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CartErrorCode errorCode() {
        return errorCode;
    }
}
