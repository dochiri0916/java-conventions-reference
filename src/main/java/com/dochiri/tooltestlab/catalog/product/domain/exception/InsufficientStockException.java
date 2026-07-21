package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class InsufficientStockException extends RuntimeException {

    private final int currentStock;
    private final int requestedQuantity;

    public InsufficientStockException(
            final int currentStock,
            final int requestedQuantity
    ) {
        super(
                "상품 재고가 부족합니다. 현재 재고는 %d개이고 요청 수량은 %d개입니다."
                        .formatted(currentStock, requestedQuantity)
        );

        this.currentStock = currentStock;
        this.requestedQuantity = requestedQuantity;
    }

    public int currentStock() {
        return currentStock;
    }

    public int requestedQuantity() {
        return requestedQuantity;
    }

}
