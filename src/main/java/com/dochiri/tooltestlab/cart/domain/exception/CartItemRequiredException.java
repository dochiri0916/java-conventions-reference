package com.dochiri.tooltestlab.cart.domain.exception;

public final class CartItemRequiredException extends CartDomainException {

    public CartItemRequiredException() {
        super("장바구니 상품은 필수입니다.");
    }

}
