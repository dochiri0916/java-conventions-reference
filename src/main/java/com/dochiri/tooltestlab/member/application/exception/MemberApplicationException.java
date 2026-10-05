package com.dochiri.tooltestlab.member.application.exception;

public abstract sealed class MemberApplicationException extends RuntimeException
        permits DuplicateEmailException,
        LoginFailedException,
        MemberNotFoundException,
        InvalidCommandException {
    protected MemberApplicationException(final String message) {
        super(message);
    }

    protected MemberApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
