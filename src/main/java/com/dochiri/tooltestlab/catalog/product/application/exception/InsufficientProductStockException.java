package com.dochiri.tooltestlab.catalog.product.application.exception;

public final class InsufficientProductStockException extends ProductApplicationException {

    public InsufficientProductStockException() {
        super("상품 재고가 부족합니다.");
    }

}
