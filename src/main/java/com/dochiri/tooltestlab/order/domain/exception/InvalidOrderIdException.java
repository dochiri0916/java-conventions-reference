package com.dochiri.tooltestlab.order.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidOrderIdException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final OrderDomainErrorCode errorCode;

    private InvalidOrderIdException(final OrderDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidOrderIdException invalidOrderId() {
        return new InvalidOrderIdException(OrderDomainErrorCode.INVALID_ORDER_ID);
    }

    public OrderDomainErrorCode code() {
        return errorCode;
    }
}
