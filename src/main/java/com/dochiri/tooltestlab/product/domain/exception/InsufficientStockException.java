package com.dochiri.tooltestlab.product.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(int currentStock, int requested) {
        super("재고가 부족합니다. 현재 재고: %d, 요청 수량: %d".formatted(currentStock, requested));
        requireNonNull(currentStock);
        requireNonNull(requested);
    }

}
