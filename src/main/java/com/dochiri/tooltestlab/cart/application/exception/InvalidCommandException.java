package com.dochiri.tooltestlab.cart.application.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidCommandException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CartErrorCode errorCode;
    private final String invalidCommandReason;

    private InvalidCommandException(final CartErrorCode errorCode, final String detail) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.invalidCommandReason = requireNonNull(detail);
    }

    public static InvalidCommandException invalidInput(final String detail) {
        return new InvalidCommandException(CartErrorCode.INVALID_INPUT, detail);
    }

    public CartErrorCode code() {
        return errorCode;
    }

    public String detail() {
        return invalidCommandReason;
    }
}
