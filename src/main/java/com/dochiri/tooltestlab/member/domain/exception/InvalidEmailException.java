package com.dochiri.tooltestlab.member.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidEmailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MemberDomainErrorCode errorCode;

    private InvalidEmailException(final MemberDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidEmailException invalidEmail() {
        return new InvalidEmailException(MemberDomainErrorCode.INVALID_EMAIL);
    }

    public MemberDomainErrorCode code() {
        return errorCode;
    }
}
