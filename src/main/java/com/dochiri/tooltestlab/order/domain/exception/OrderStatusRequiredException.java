package com.dochiri.tooltestlab.order.domain.exception;

public final class OrderStatusRequiredException extends OrderDomainException {

    public OrderStatusRequiredException() {
        super("주문 상태는 필수입니다.");
    }

}
