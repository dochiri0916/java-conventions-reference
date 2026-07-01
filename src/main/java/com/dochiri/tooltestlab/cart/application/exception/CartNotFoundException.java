package com.dochiri.tooltestlab.cart.application.exception;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public final class CartNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CartErrorCode errorCode;
    private final String memberPublicId;

    private CartNotFoundException(final CartErrorCode errorCode, final String memberPublicId) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.memberPublicId = requireNonNull(memberPublicId);
    }

    public static CartNotFoundException cartNotFound(final String memberPublicId) {
        return new CartNotFoundException(CartErrorCode.CART_NOT_FOUND, memberPublicId);
    }

    public CartErrorCode code() {
        return errorCode;
    }

    public String memberPublicId() {
        return memberPublicId;
    }
}
