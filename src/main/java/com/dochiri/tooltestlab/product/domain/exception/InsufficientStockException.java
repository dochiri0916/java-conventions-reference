package com.dochiri.tooltestlab.product.domain.exception;

public final class InsufficientStockException extends ProductDomainException {

    public InsufficientStockException(final int currentStock, final int requested) {
        super(ProductDomainErrorCode.LOW_STOCK,
                "재고가 부족합니다. 현재 재고: %d, 요청 수량: %d".formatted(currentStock, requested));
    }
}
