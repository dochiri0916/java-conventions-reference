package com.dochiri.tooltestlab.cart.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidCartException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CartDomainErrorCode errorCode;

    private InvalidCartException(final CartDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidCartException invalidCart() {
        return new InvalidCartException(CartDomainErrorCode.INVALID_CART);
    }

    public static InvalidCartException required() {
        return new InvalidCartException(CartDomainErrorCode.CART_REQUIRED);
    }

    public CartDomainErrorCode code() {
        return errorCode;
    }
}
