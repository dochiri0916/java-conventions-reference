package com.dochiri.tooltestlab.order.domain.exception;

import com.dochiri.tooltestlab.order.domain.model.OrderId;

import static java.util.Objects.requireNonNull;

public final class InvalidOrderStatusException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final OrderDomainErrorCode errorCode;
    private final OrderId orderIdField;
    private final String expectedField;
    private final String actualField;

    private InvalidOrderStatusException(final OrderDomainErrorCode errorCode,
                                        final OrderId orderId,
                                        final String expected,
                                        final String actual) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.orderIdField = requireNonNull(orderId);
        this.expectedField = requireNonNull(expected);
        this.actualField = requireNonNull(actual);
    }

    public static InvalidOrderStatusException badOrderStatus(final OrderId orderId,
                                                              final String expected,
                                                              final String actual) {
        return new InvalidOrderStatusException(OrderDomainErrorCode.BAD_ORDER_STATUS, orderId, expected, actual);
    }

    public OrderDomainErrorCode code() {
        return errorCode;
    }

    public OrderId orderId() {
        return orderIdField;
    }

    public String expected() {
        return expectedField;
    }

    public String actual() {
        return actualField;
    }
}
