package com.dochiri.tooltestlab.member.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidMemberException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MemberDomainErrorCode errorCode;

    private InvalidMemberException(final MemberDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidMemberException invalidMember() {
        return new InvalidMemberException(MemberDomainErrorCode.INVALID_MEMBER);
    }

    public static InvalidMemberException required() {
        return new InvalidMemberException(MemberDomainErrorCode.MEMBER_REQUIRED);
    }

    public MemberDomainErrorCode code() {
        return errorCode;
    }
}
