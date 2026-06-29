package com.dochiri.tooltestlab.cart.domain.exception;

public abstract class CartDomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CartDomainErrorCode code;

    protected CartDomainException(final CartDomainErrorCode code, final String message) {
        super(message);
        this.code = code;
    }

    public CartDomainErrorCode errorCode() {
        return code;
    }
}
