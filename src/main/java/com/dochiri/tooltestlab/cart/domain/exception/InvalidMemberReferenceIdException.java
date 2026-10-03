package com.dochiri.tooltestlab.cart.domain.exception;

public final class InvalidMemberReferenceIdException extends CartDomainException {

    public InvalidMemberReferenceIdException() {
        super("유효하지 않은 회원 참조 ID입니다.");
    }
}
