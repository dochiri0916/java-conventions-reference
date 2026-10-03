package com.dochiri.tooltestlab.order.domain.exception;

public final class InvalidOrderLinesException extends OrderDomainException {

    public InvalidOrderLinesException() {
        super("주문 상품 목록이 올바르지 않습니다.");
    }

}
