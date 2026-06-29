package com.dochiri.tooltestlab.order.domain.exception;

public final class InvalidOrderLinesException extends OrderDomainException {

    public InvalidOrderLinesException(final String detail) {
        super(OrderDomainErrorCode.BAD_ORDER_LINES, detail);
    }
}
