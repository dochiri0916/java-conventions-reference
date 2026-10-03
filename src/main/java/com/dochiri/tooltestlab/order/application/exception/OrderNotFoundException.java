package com.dochiri.tooltestlab.order.application.exception;

public final class OrderNotFoundException extends OrderApplicationException {

    public OrderNotFoundException() {
        super("주문을 찾을 수 없습니다.");
    }

}
