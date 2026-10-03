package com.dochiri.tooltestlab.cart.application.exception;

public final class CartNotFoundException extends CartApplicationException {

    public CartNotFoundException() {
        super("장바구니를 찾을 수 없습니다.");
    }

}
