package com.dochiri.tooltestlab.cart.domain.exception;

public final class InvalidCartItemException extends CartDomainException {

    public InvalidCartItemException(final String detail) {
        super(CartDomainErrorCode.INVALID_CART_ITEM, detail);
    }
}
