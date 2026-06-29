package com.dochiri.tooltestlab.cart.domain.exception;

public final class InvalidCartIdException extends CartDomainException {

    public InvalidCartIdException() {
        super(CartDomainErrorCode.INVALID_CART_ID, "장바구니 ID는 빈 값일 수 없습니다.");
    }
}
