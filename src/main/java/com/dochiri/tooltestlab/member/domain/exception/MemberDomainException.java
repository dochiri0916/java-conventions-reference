package com.dochiri.tooltestlab.member.domain.exception;

public abstract class MemberDomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MemberDomainErrorCode code;

    protected MemberDomainException(final MemberDomainErrorCode code, final String message) {
        super(message);
        this.code = code;
    }

    public MemberDomainErrorCode errorCode() {
        return code;
    }
}
