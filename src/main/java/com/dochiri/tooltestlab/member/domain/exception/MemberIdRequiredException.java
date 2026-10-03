package com.dochiri.tooltestlab.member.domain.exception;

public final class MemberIdRequiredException extends MemberDomainException {

    public MemberIdRequiredException() {
        super("회원 ID는 필수입니다.");
    }

}
