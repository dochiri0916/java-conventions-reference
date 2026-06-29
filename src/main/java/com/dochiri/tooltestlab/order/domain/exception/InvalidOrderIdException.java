package com.dochiri.tooltestlab.order.domain.exception;

public final class InvalidOrderIdException extends OrderDomainException {

    public InvalidOrderIdException() {
        super(OrderDomainErrorCode.INVALID_ORDER_ID, "주문 ID는 빈 값일 수 없습니다.");
    }
}
