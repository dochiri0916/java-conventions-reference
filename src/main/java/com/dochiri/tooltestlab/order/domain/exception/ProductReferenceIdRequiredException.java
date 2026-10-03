package com.dochiri.tooltestlab.order.domain.exception;

public final class ProductReferenceIdRequiredException extends OrderDomainException {

    public ProductReferenceIdRequiredException() {
        super("상품 ID는 필수입니다.");
    }

}
