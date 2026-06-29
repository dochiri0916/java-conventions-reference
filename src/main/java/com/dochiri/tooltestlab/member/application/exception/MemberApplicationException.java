package com.dochiri.tooltestlab.member.application.exception;

public abstract class MemberApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MemberErrorCode errorCode;

    protected MemberApplicationException(final MemberErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public MemberErrorCode errorCode() {
        return errorCode;
    }
}
