package com.dochiri.tooltestlab.review.domain.exception;

public final class InvalidMemberReferenceIdException extends ReviewDomainException {

    public InvalidMemberReferenceIdException() {
        super("유효하지 않은 회원 참조 ID입니다.");
    }
}
