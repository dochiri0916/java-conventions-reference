package com.dochiri.tooltestlab.member.domain.exception;

public final class InvalidPasswordException extends MemberDomainException {

    public InvalidPasswordException() {
        super("유효하지 않은 비밀번호입니다.");
    }

}
