package com.dochiri.tooltestlab.cart.domain.exception;

public final class InvalidCartRevisionException extends CartDomainException {

    public InvalidCartRevisionException() {
        super("장바구니 revision은 음수일 수 없습니다.");
    }

}
