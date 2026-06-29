package com.dochiri.tooltestlab.member.domain.exception;

public final class InvalidMemberIdException extends MemberDomainException {

    public InvalidMemberIdException() {
        super(MemberDomainErrorCode.INVALID_MEMBER_ID, "회원 ID는 빈 값일 수 없습니다.");
    }
}
