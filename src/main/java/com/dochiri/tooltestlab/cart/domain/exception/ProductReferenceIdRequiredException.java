package com.dochiri.tooltestlab.cart.domain.exception;

public final class ProductReferenceIdRequiredException extends CartDomainException {

    public ProductReferenceIdRequiredException() {
        super("상품 ID는 필수입니다.");
    }

}
