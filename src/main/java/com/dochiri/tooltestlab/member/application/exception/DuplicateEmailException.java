package com.dochiri.tooltestlab.member.application.exception;

import static java.util.Objects.requireNonNull;

public final class DuplicateEmailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MemberErrorCode errorCode;
    private final String email;

    private DuplicateEmailException(final MemberErrorCode errorCode, final String email) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.email = requireNonNull(email);
    }

    public static DuplicateEmailException duplicateEmail(final String email) {
        return new DuplicateEmailException(MemberErrorCode.DUPLICATE_EMAIL, email);
    }

    public MemberErrorCode code() {
        return errorCode;
    }

    public String email() {
        return email;
    }
}
