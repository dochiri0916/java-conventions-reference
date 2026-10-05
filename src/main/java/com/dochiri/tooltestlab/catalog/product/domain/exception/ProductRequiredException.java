package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class ProductRequiredException extends ProductDomainException {

    public ProductRequiredException() {
        super("상품은 필수입니다.");
    }

}
