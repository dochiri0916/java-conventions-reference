package com.dochiri.tooltestlab.order.domain.exception;

public abstract class OrderDomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final OrderDomainErrorCode code;

    protected OrderDomainException(final OrderDomainErrorCode code, final String message) {
        super(message);
        this.code = code;
    }

    public OrderDomainErrorCode errorCode() {
        return code;
    }
}
