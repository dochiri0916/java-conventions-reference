package com.dochiri.tooltestlab.order.application.exception;

public final class EmptyCartException extends OrderApplicationException {

    public EmptyCartException() {
        super("장바구니에 담긴 상품이 없습니다.");
    }

}
