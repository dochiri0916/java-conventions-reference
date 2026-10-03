package com.dochiri.tooltestlab.member.domain.exception;

public final class PasswordRequiredException extends MemberDomainException {

    public PasswordRequiredException() {
        super("비밀번호는 필수입니다.");
    }

}
