package com.dochiri.tooltestlab.global.error;

import org.springframework.validation.FieldError;

public record FieldErrorResponse(
        String field,
        String message,
        Object rejectedValue
) {
    public static FieldErrorResponse from(FieldError fieldError) {
        return new FieldErrorResponse(
                fieldError.getField(),
                fieldError.getDefaultMessage(),
                fieldError.getRejectedValue()
        );
    }
}
