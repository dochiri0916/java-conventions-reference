package com.dochiri.tooltestlab.member.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InvalidMemberIdException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MemberDomainErrorCode errorCode;

    private InvalidMemberIdException(final MemberDomainErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
    }

    public static InvalidMemberIdException invalidMemberId() {
        return new InvalidMemberIdException(MemberDomainErrorCode.INVALID_MEMBER_ID);
    }

    public MemberDomainErrorCode code() {
        return errorCode;
    }
}
