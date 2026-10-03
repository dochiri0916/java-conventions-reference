package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class ProductNameRequiredException extends RuntimeException {

    public ProductNameRequiredException() {
        super("상품명은 필수입니다.");
    }

}
