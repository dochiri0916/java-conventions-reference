package com.dochiri.tooltestlab.order.application.exception;

import static java.util.Objects.requireNonNull;

public final class OrderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final OrderApplicationErrorCode errorCode;
    private final String missingOrderId;

    private OrderNotFoundException(final OrderApplicationErrorCode errorCode, final String missingOrderId) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.missingOrderId = requireNonNull(missingOrderId);
    }

    public static OrderNotFoundException orderNotFound(final String orderId) {
        return new OrderNotFoundException(OrderApplicationErrorCode.ORDER_NOT_FOUND, orderId);
    }

    public OrderApplicationErrorCode code() {
        return errorCode;
    }

    public String orderId() {
        return missingOrderId;
    }
}
