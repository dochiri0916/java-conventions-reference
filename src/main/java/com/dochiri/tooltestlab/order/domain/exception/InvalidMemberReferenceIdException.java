package com.dochiri.tooltestlab.order.domain.exception;

public final class InvalidMemberReferenceIdException extends OrderDomainException {

    public InvalidMemberReferenceIdException() {
        super("유효하지 않은 회원 참조 ID입니다.");
    }
}
