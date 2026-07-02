package com.dochiri.tooltestlab.cart.application.exception;

import static java.util.Objects.requireNonNull;

public final class CartNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CartErrorCode errorCode;
    private final String missingMemberId;

    private CartNotFoundException(final CartErrorCode errorCode, final String missingMemberId) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.missingMemberId = requireNonNull(missingMemberId);
    }

    public static CartNotFoundException cartNotFound(final String memberId) {
        return new CartNotFoundException(CartErrorCode.CART_NOT_FOUND, memberId);
    }

    public CartErrorCode code() {
        return errorCode;
    }

    public String memberId() {
        return missingMemberId;
    }
}
