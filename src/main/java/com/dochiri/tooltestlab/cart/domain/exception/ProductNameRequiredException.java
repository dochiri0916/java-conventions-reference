package com.dochiri.tooltestlab.cart.domain.exception;

public final class ProductNameRequiredException extends CartDomainException {

    public ProductNameRequiredException() {
        super("상품명은 필수입니다.");
    }

}
