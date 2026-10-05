package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class ProductStatusRequiredException extends ProductDomainException {

    public ProductStatusRequiredException() {
        super("상품 상태는 필수입니다.");
    }

}
