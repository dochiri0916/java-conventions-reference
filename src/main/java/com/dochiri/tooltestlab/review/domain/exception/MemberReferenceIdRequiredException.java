package com.dochiri.tooltestlab.review.domain.exception;

public final class MemberReferenceIdRequiredException extends ReviewDomainException {

    public MemberReferenceIdRequiredException() {
        super("회원 ID는 필수입니다.");
    }

}
