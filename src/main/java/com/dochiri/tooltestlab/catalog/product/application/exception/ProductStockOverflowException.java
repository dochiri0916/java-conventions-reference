package com.dochiri.tooltestlab.catalog.product.application.exception;

public final class ProductStockOverflowException extends ProductApplicationException {

    public ProductStockOverflowException() {
        super("상품 재고가 허용 범위를 초과했습니다.");
    }

}
