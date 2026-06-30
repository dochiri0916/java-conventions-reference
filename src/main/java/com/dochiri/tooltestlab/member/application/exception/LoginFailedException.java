package com.dochiri.tooltestlab.member.application.exception;

import static java.util.Objects.requireNonNull;

public final class LoginFailedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MemberErrorCode errorCode;

    private LoginFailedException(final MemberErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static LoginFailedException loginFailed() {
        return new LoginFailedException(MemberErrorCode.LOGIN_FAILED);
    }

    public MemberErrorCode code() {
        return errorCode;
    }
}
