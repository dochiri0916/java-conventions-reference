package com.dochiri.tooltestlab.review.domain.exception;

public final class ProductReferenceIdRequiredException extends ReviewDomainException {

    public ProductReferenceIdRequiredException() {
        super("상품 ID는 필수입니다.");
    }

}
