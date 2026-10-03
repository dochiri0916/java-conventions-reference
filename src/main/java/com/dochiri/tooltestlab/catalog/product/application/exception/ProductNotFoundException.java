package com.dochiri.tooltestlab.catalog.product.application.exception;

public final class ProductNotFoundException extends ProductApplicationException {

    public ProductNotFoundException() {
        super("상품을 찾을 수 없습니다.");
    }

}
