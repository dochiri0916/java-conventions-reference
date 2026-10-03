package com.dochiri.tooltestlab.order.domain.exception;

public final class OrderIdRequiredException extends OrderDomainException {

    public OrderIdRequiredException() {
        super("주문 ID는 필수입니다.");
    }

}
