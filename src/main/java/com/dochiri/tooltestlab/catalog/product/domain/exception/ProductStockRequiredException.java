package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class ProductStockRequiredException extends RuntimeException {

    public ProductStockRequiredException() {
        super("상품 재고는 필수입니다.");
    }

}
