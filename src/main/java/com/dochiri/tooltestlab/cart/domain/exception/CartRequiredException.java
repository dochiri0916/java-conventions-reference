package com.dochiri.tooltestlab.cart.domain.exception;

public final class CartRequiredException extends CartDomainException {

    public CartRequiredException() {
        super("장바구니 정보는 필수입니다.");
    }

}
