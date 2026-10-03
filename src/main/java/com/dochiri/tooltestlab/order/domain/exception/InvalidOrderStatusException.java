package com.dochiri.tooltestlab.order.domain.exception;

public final class InvalidOrderStatusException extends OrderDomainException {

    public InvalidOrderStatusException() {
        super("현재 주문 상태에서는 처리할 수 없습니다.");
    }

}
