package com.dochiri.tooltestlab.order.application.exception;

public final class CheckoutAlreadyRequestedException extends OrderApplicationException {
    public CheckoutAlreadyRequestedException() {
        super("이미 주문을 요청한 장바구니입니다.");
    }
}
