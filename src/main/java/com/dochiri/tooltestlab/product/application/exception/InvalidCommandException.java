package com.dochiri.tooltestlab.product.application.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidCommandException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductErrorCode errorCode;
    private final String detailField;

    private InvalidCommandException(final ProductErrorCode errorCode, final String detail) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.detailField = requireNonNull(detail);
    }

    public static InvalidCommandException invalidInput(final String detail) {
        return new InvalidCommandException(ProductErrorCode.INVALID_INPUT, detail);
    }

    public ProductErrorCode code() {
        return errorCode;
    }

    public String detail() {
        return detailField;
    }
}
