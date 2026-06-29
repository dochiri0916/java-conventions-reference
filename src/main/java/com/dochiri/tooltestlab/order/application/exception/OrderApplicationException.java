package com.dochiri.tooltestlab.order.application.exception;

public abstract class OrderApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final OrderApplicationErrorCode errorCode;

    protected OrderApplicationException(final OrderApplicationErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public OrderApplicationErrorCode errorCode() {
        return errorCode;
    }
}
