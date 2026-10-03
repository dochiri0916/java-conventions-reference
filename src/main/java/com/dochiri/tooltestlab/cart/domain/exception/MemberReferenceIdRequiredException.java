package com.dochiri.tooltestlab.cart.domain.exception;

public final class MemberReferenceIdRequiredException extends CartDomainException {

    public MemberReferenceIdRequiredException() {
        super("회원 ID는 필수입니다.");
    }

}
