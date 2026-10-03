package com.dochiri.tooltestlab.cart.domain.exception;

public final class InvalidCartItemException extends CartDomainException {

    public InvalidCartItemException() {
        super("장바구니 상품이 올바르지 않습니다.");
    }

}
