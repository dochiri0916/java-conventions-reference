package com.dochiri.tooltestlab.catalog.product.domain.vo;

public enum StockStatus {

    IN_STOCK("재고 있음"),
    SOLD_OUT("품절");

    private final String displayName;

    StockStatus(final String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
