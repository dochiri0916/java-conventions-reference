package com.dochiri.tooltestlab.member.domain.exception;

public final class InvalidEmailException extends MemberDomainException {

    public InvalidEmailException() {
        super(MemberDomainErrorCode.INVALID_EMAIL, "이메일은 빈 값일 수 없습니다.");
    }
}
