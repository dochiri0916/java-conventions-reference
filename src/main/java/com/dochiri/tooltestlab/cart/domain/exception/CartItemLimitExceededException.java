package com.dochiri.tooltestlab.cart.domain.exception;

import static java.util.Objects.requireNonNull;

public final class CartItemLimitExceededException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CartDomainErrorCode errorCode;
    private final int actualSizeField;

    private CartItemLimitExceededException(final CartDomainErrorCode errorCode, final int actualSize) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.actualSizeField = actualSize;
    }

    public static CartItemLimitExceededException tooManyItems(final int actualSize) {
        return new CartItemLimitExceededException(CartDomainErrorCode.TOO_MANY_ITEMS, actualSize);
    }

    public CartDomainErrorCode code() {
        return errorCode;
    }

    public int actualSize() {
        return actualSizeField;
    }
}
