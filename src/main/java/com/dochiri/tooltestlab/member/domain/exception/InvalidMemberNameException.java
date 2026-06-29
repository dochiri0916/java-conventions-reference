package com.dochiri.tooltestlab.member.domain.exception;

public final class InvalidMemberNameException extends MemberDomainException {

    public InvalidMemberNameException() {
        super(MemberDomainErrorCode.INVALID_MBR_NAME, "이름은 빈 값일 수 없습니다.");
    }
}
