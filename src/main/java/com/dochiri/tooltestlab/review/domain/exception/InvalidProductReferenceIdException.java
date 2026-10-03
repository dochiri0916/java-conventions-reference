package com.dochiri.tooltestlab.review.domain.exception;

public final class InvalidProductReferenceIdException extends ReviewDomainException {

    public InvalidProductReferenceIdException() {
        super("유효하지 않은 상품 참조 ID입니다.");
    }
}
