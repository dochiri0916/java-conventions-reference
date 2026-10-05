package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class ProductPriceRequiredException extends ProductDomainException {

    public ProductPriceRequiredException() {
        super("상품 가격은 필수입니다.");
    }

}
