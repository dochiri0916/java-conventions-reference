package com.dochiri.tooltestlab.member.domain.exception;

public final class EmailRequiredException extends MemberDomainException {

    public EmailRequiredException() {
        super("이메일은 필수입니다.");
    }

}
