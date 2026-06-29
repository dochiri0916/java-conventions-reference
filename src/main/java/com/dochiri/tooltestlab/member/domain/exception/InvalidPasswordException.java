package com.dochiri.tooltestlab.member.domain.exception;

public final class InvalidPasswordException extends MemberDomainException {

    public InvalidPasswordException() {
        super(MemberDomainErrorCode.INVALID_PASSWORD, "비밀번호는 빈 값일 수 없습니다.");
    }
}
