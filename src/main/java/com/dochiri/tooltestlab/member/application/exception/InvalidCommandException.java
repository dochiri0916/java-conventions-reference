package com.dochiri.tooltestlab.member.application.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidCommandException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MemberErrorCode errorCode;

    private InvalidCommandException(final MemberErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidCommandException emailRequired() {
        return new InvalidCommandException(MemberErrorCode.EMAIL_REQUIRED);
    }

    public static InvalidCommandException passwordRequired() {
        return new InvalidCommandException(MemberErrorCode.PASSWORD_REQUIRED);
    }

    public static InvalidCommandException nameRequired() {
        return new InvalidCommandException(MemberErrorCode.NAME_REQUIRED);
    }

    public MemberErrorCode code() {
        return errorCode;
    }
}
