package com.dochiri.tooltestlab.member.domain.exception;

public final class InvalidMemberIdException extends MemberDomainException {

    public InvalidMemberIdException() {
        super("유효하지 않은 회원 ID입니다.");
    }

}
