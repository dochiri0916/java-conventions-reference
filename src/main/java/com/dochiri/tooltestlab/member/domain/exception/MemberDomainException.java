package com.dochiri.tooltestlab.member.domain.exception;

public abstract sealed class MemberDomainException extends RuntimeException
        permits EmailRequiredException,
        InvalidEmailException,
        MemberIdRequiredException,
        InvalidMemberIdException,
        PasswordRequiredException,
        InvalidPasswordException,
        MemberNameRequiredException,
        InvalidMemberNameException,
        MemberStatusRequiredException {
    protected MemberDomainException(final String message) {
        super(message);
    }

}
