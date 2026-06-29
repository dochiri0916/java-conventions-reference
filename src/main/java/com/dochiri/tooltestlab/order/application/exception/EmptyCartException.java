package com.dochiri.tooltestlab.order.application.exception;

public final class EmptyCartException extends OrderApplicationException {

    public EmptyCartException() {
        super(OrderApplicationErrorCode.EMPTY_CART, "장바구니가 비어 있습니다.");
    }
}
