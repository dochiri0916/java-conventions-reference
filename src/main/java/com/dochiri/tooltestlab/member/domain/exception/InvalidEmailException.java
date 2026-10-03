package com.dochiri.tooltestlab.member.domain.exception;

public final class InvalidEmailException extends MemberDomainException {

    public InvalidEmailException() {
        super("유효하지 않은 이메일입니다.");
    }

}
