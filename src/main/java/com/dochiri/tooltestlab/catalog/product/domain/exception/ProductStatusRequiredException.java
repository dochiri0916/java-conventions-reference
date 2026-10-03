package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class ProductStatusRequiredException extends RuntimeException {

    public ProductStatusRequiredException() {
        super("상품 상태는 필수입니다.");
    }

}
