package com.dochiri.tooltestlab.order.domain.exception;

public final class InvalidQuantityException extends OrderDomainException {

    public InvalidQuantityException(final int value) {
        super(OrderDomainErrorCode.INVALID_QUANTITY, "수량은 1 이상이어야 합니다. 입력값: " + value);
    }
}
