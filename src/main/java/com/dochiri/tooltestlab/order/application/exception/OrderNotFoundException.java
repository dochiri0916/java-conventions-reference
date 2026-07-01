package com.dochiri.tooltestlab.order.application.exception;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public final class OrderNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final OrderApplicationErrorCode errorCode;
    private final String publicId;

    private OrderNotFoundException(final OrderApplicationErrorCode errorCode, final String publicId) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.publicId = requireNonNull(publicId);
    }

    public static OrderNotFoundException orderNotFound(final String publicId) {
        return new OrderNotFoundException(OrderApplicationErrorCode.ORDER_NOT_FOUND, publicId);
    }

    public OrderApplicationErrorCode code() {
        return errorCode;
    }

    public String publicId() {
        return publicId;
    }
}
