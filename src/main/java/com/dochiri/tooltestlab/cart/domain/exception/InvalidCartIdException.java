package com.dochiri.tooltestlab.cart.domain.exception;

public final class InvalidCartIdException extends CartDomainException {

    public InvalidCartIdException() {
        super("유효하지 않은 장바구니 ID입니다.");
    }

}
