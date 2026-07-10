package com.dochiri.tooltestlab.category.application.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidCommandException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final CategoryErrorCode errorCode;
    private final String invalidCommandReason;

    private InvalidCommandException(final CategoryErrorCode errorCode, final String detail) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.invalidCommandReason = requireNonNull(detail);
    }

    public static InvalidCommandException invalidInput(final String detail) {
        return new InvalidCommandException(CategoryErrorCode.INVALID_INPUT, detail);
    }

    public CategoryErrorCode code() {
        return errorCode;
    }

    public String detail() {
        return invalidCommandReason;
    }
}
