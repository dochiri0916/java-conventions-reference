package com.dochiri.tooltestlab.order.application.exception;

import static java.util.Objects.requireNonNull;

public final class EmptyCartException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final OrderApplicationErrorCode errorCode;

    private EmptyCartException(final OrderApplicationErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static EmptyCartException emptyCart() {
        return new EmptyCartException(OrderApplicationErrorCode.EMPTY_CART);
    }

    public OrderApplicationErrorCode code() {
        return errorCode;
    }
}
