package com.dochiri.tooltestlab.member.application.exception;

import static java.util.Objects.requireNonNull;

public final class AuthenticationRequiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MemberErrorCode errorCode;

    private AuthenticationRequiredException(final MemberErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static AuthenticationRequiredException authenticationRequired() {
        return new AuthenticationRequiredException(MemberErrorCode.AUTHENTICATION_REQUIRED);
    }

    public MemberErrorCode code() {
        return errorCode;
    }
}
