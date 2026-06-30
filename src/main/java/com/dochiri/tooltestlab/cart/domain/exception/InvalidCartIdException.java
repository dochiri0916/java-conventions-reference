package com.dochiri.tooltestlab.cart.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidCartIdException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CartDomainErrorCode errorCode;

    private InvalidCartIdException(final CartDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidCartIdException invalidCartId() {
        return new InvalidCartIdException(CartDomainErrorCode.INVALID_CART_ID);
    }

    public CartDomainErrorCode code() {
        return errorCode;
    }
}
