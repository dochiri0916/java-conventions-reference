package com.dochiri.tooltestlab.cart.application.exception;

public final class CartNotFoundException extends CartApplicationException {

    public CartNotFoundException(final String memberPublicId) {
        super(CartErrorCode.CART_NOT_FOUND,
                "해당 회원의 장바구니를 찾을 수 없습니다. memberPublicId=" + memberPublicId);
    }
}
