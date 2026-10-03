package com.dochiri.tooltestlab.order.domain.exception;

public final class InvalidOrderIdException extends OrderDomainException {

    public InvalidOrderIdException() {
        super("유효하지 않은 주문 ID입니다.");
    }

}
