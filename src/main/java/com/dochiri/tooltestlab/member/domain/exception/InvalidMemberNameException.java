package com.dochiri.tooltestlab.member.domain.exception;

public final class InvalidMemberNameException extends MemberDomainException {

    public InvalidMemberNameException() {
        super("유효하지 않은 회원 이름입니다.");
    }

}
