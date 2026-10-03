package com.dochiri.tooltestlab.order.domain.exception;

public final class MemberReferenceIdRequiredException extends OrderDomainException {

    public MemberReferenceIdRequiredException() {
        super("회원 ID는 필수입니다.");
    }

}
