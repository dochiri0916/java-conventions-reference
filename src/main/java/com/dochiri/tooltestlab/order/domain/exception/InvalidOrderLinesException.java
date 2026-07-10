package com.dochiri.tooltestlab.order.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidOrderLinesException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final OrderDomainErrorCode errorCode;
    private final String invalidOrderLinesReason;

    private InvalidOrderLinesException(final OrderDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.invalidOrderLinesReason = null;
    }

    private InvalidOrderLinesException(final OrderDomainErrorCode errorCode, final String detail) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.invalidOrderLinesReason = requireNonNull(detail);
    }

    public static InvalidOrderLinesException badOrderLines(final String detail) {
        return new InvalidOrderLinesException(OrderDomainErrorCode.BAD_ORDER_LINES, detail);
    }

    public static InvalidOrderLinesException required() {
        return new InvalidOrderLinesException(OrderDomainErrorCode.ORDER_LINES_REQUIRED);
    }

    public OrderDomainErrorCode code() {
        return errorCode;
    }

    public String detail() {
        return invalidOrderLinesReason;
    }
}
