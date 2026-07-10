package com.dochiri.tooltestlab.order.domain.exception;

import com.dochiri.tooltestlab.order.domain.model.OrderId;

import static java.util.Objects.requireNonNull;

public final class InvalidOrderStatusException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final OrderDomainErrorCode errorCode;
    private final transient OrderId invalidOrderIdentifier;
    private final String expectedStatus;
    private final String actualStatus;

    private InvalidOrderStatusException(final OrderDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.invalidOrderIdentifier = null;
        this.expectedStatus = null;
        this.actualStatus = null;
    }

    private InvalidOrderStatusException(final OrderDomainErrorCode errorCode,
                                        final OrderId orderId,
                                        final String expected,
                                        final String actual) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.invalidOrderIdentifier = requireNonNull(orderId);
        this.expectedStatus = requireNonNull(expected);
        this.actualStatus = requireNonNull(actual);
    }

    public static InvalidOrderStatusException badOrderStatus(final OrderId orderId,
                                                              final String expected,
                                                              final String actual) {
        return new InvalidOrderStatusException(OrderDomainErrorCode.BAD_ORDER_STATUS, orderId, expected, actual);
    }

    public static InvalidOrderStatusException required() {
        return new InvalidOrderStatusException(OrderDomainErrorCode.ORDER_STATUS_REQUIRED);
    }

    public OrderDomainErrorCode code() {
        return errorCode;
    }

    public OrderId orderId() {
        return invalidOrderIdentifier;
    }

    public String expected() {
        return expectedStatus;
    }

    public String actual() {
        return actualStatus;
    }
}
