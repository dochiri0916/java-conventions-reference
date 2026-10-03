package com.dochiri.tooltestlab.cart.domain.exception;

public final class CartIdRequiredException extends CartDomainException {

    public CartIdRequiredException() {
        super("장바구니 ID는 필수입니다.");
    }

}
