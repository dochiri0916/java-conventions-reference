package com.dochiri.tooltestlab.catalog.product.domain.exception;


public final class StockOverflowException extends ProductDomainException {

    private final int currentStock;
    private final int requestedQuantity;

    public StockOverflowException(final int currentStock, final int requestedQuantity) {
        super("상품 재고가 허용된 최대 수량을 초과합니다.");
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
