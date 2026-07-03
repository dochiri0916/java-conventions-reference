package com.dochiri.tooltestlab.global.error;

import static java.util.Objects.requireNonNull;

public final class AuthenticationRequiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final GlobalErrorCode errorCode;

    private AuthenticationRequiredException(final GlobalErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static AuthenticationRequiredException authenticationRequired() {
        return new AuthenticationRequiredException(GlobalErrorCode.AUTHENTICATION_REQUIRED);
    }

    public GlobalErrorCode code() {
        return errorCode;
    }
}
