package com.dochiri.tooltestlab.review.application.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidCommandException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ReviewApplicationErrorCode errorCode;
    private final String detailField;

    private InvalidCommandException(final ReviewApplicationErrorCode errorCode, final String detail) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.detailField = requireNonNull(detail);
    }

    public static InvalidCommandException invalidInput(final String detail) {
        return new InvalidCommandException(ReviewApplicationErrorCode.INVALID_INPUT, detail);
    }

    public ReviewApplicationErrorCode code() {
        return errorCode;
    }

    public String detail() {
        return detailField;
    }
}
