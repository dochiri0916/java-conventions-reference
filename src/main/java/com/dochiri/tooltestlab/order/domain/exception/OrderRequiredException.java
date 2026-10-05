package com.dochiri.tooltestlab.order.domain.exception;

public final class OrderRequiredException extends OrderDomainException {

    public OrderRequiredException() {
        super("주문은 필수입니다.");
    }

}
