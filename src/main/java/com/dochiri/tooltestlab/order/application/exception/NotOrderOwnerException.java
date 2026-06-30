package com.dochiri.tooltestlab.order.application.exception;

import static java.util.Objects.requireNonNull;

public final class NotOrderOwnerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final OrderApplicationErrorCode errorCode;

    private NotOrderOwnerException(final OrderApplicationErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static NotOrderOwnerException notOrderOwner() {
        return new NotOrderOwnerException(OrderApplicationErrorCode.NOT_ORDER_OWNER);
    }

    public OrderApplicationErrorCode code() {
        return errorCode;
    }
}
