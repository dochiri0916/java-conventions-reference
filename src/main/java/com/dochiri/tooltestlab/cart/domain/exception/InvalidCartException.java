package com.dochiri.tooltestlab.cart.domain.exception;

public final class InvalidCartException extends CartDomainException {

    public InvalidCartException() {
        super("장바구니 정보가 올바르지 않습니다.");
    }

}
