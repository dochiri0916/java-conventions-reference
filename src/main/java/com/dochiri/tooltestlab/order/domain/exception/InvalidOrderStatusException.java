package com.dochiri.tooltestlab.order.domain.exception;

public class InvalidOrderStatusException extends OrderDomainException {

    public InvalidOrderStatusException() {
        super(OrderDomainErrorCode.INVALID_ORDER_STATUS);
    }

    public InvalidOrderStatusException(String message) {
        super(OrderDomainErrorCode.INVALID_ORDER_STATUS, message);
    }

}
