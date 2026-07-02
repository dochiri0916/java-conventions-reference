package com.dochiri.tooltestlab.cart.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidCartItemException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CartDomainErrorCode errorCode;
    private final String detailField;

    private InvalidCartItemException(final CartDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.detailField = null;
    }

    private InvalidCartItemException(final CartDomainErrorCode errorCode, final String detail) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.detailField = requireNonNull(detail);
    }

    public static InvalidCartItemException invalidCartItem(final String detail) {
        return new InvalidCartItemException(CartDomainErrorCode.INVALID_CART_ITEM, detail);
    }

    public static InvalidCartItemException required() {
        return new InvalidCartItemException(CartDomainErrorCode.CART_ITEM_REQUIRED);
    }

    public CartDomainErrorCode code() {
        return errorCode;
    }

    public String detail() {
        return detailField;
    }
}
