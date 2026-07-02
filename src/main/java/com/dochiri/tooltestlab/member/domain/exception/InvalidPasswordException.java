package com.dochiri.tooltestlab.member.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidPasswordException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MemberDomainErrorCode errorCode;

    private InvalidPasswordException(final MemberDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidPasswordException invalidPassword() {
        return new InvalidPasswordException(MemberDomainErrorCode.INVALID_PASSWORD);
    }

    public static InvalidPasswordException required() {
        return new InvalidPasswordException(MemberDomainErrorCode.PASSWORD_REQUIRED);
    }

    public MemberDomainErrorCode code() {
        return errorCode;
    }
}
