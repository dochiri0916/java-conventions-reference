package com.dochiri.tooltestlab.member.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidMemberNameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MemberDomainErrorCode errorCode;

    private InvalidMemberNameException(final MemberDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidMemberNameException invalidMbrName() {
        return new InvalidMemberNameException(MemberDomainErrorCode.INVALID_MBR_NAME);
    }

    public static InvalidMemberNameException required() {
        return new InvalidMemberNameException(MemberDomainErrorCode.MEMBER_NAME_REQUIRED);
    }

    public MemberDomainErrorCode code() {
        return errorCode;
    }
}
