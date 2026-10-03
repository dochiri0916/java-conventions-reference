package com.dochiri.tooltestlab.cart.domain.exception;

public final class TooManyCartItemsException extends CartDomainException {

    public TooManyCartItemsException() {
        super("장바구니 상품은 최대 100개까지 담을 수 있습니다.");
    }

}
