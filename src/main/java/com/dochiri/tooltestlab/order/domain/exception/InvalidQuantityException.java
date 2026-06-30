package com.dochiri.tooltestlab.order.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidQuantityException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final OrderDomainErrorCode errorCode;
    private final int valueField;

    private InvalidQuantityException(final OrderDomainErrorCode errorCode, final int value) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.valueField = value;
    }

    public static InvalidQuantityException invalidQuantity(final int value) {
        return new InvalidQuantityException(OrderDomainErrorCode.INVALID_QUANTITY, value);
    }

    public OrderDomainErrorCode code() {
        return errorCode;
    }

    public int value() {
        return valueField;
    }
}
