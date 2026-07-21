package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class ProductIdRequiredException extends RuntimeException {

    public ProductIdRequiredException() {
        super("상품 ID는 필수입니다.");
    }

}
