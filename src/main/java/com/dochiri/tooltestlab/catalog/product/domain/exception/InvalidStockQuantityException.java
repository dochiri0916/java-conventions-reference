package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class InvalidStockQuantityException extends RuntimeException {

    private final int currentQuantity;

    public InvalidStockQuantityException(final int currentQuantity) {
        super("재고 변경 수량은 1 이상이어야 합니다.");
        this.currentQuantity = currentQuantity;
    }

    public int quantity() {
        return currentQuantity;
    }

}
