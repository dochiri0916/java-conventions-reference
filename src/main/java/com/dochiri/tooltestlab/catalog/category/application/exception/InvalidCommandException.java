package com.dochiri.tooltestlab.catalog.category.application.exception;

import com.dochiri.tooltestlab.global.exception.ApplicationException;

import java.util.Map;

public final class InvalidCommandException extends ApplicationException {

    private InvalidCommandException(final String detail) {
        super(CategoryErrorCode.INVALID_INPUT, detail, Map.of());
    }

    public static InvalidCommandException invalidInput(final String detail) {
        return new InvalidCommandException(detail);
    }
}
