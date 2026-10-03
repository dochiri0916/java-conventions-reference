package com.dochiri.tooltestlab.order.domain.exception;

public final class OrderLinesRequiredException extends OrderDomainException {

    public OrderLinesRequiredException() {
        super("주문 상품 목록은 필수입니다.");
    }

}
