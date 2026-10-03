package com.dochiri.tooltestlab.member.domain.exception;

public final class MemberNameRequiredException extends MemberDomainException {

    public MemberNameRequiredException() {
        super("회원 이름은 필수입니다.");
    }

}
