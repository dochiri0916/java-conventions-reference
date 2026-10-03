package com.dochiri.tooltestlab.member.domain.exception;

public final class MemberStatusRequiredException extends MemberDomainException {

    public MemberStatusRequiredException() {
        super("회원 상태는 필수입니다.");
    }

}
