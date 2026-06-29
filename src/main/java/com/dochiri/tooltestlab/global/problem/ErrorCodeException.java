package com.dochiri.tooltestlab.global.problem;

public abstract class ErrorCodeException extends RuntimeException {

    protected ErrorCodeException(String message) {
        super(message);
    }

    protected ErrorCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract ErrorCode errorCode();

    public String detail() {
        return getMessage();
    }
}
