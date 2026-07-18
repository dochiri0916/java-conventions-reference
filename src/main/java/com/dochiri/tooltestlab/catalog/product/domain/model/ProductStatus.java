package com.dochiri.tooltestlab.catalog.product.domain.model;

public enum ProductStatus {
    PREPARING("판매 준비"),
    ON_SALE("판매 중"),
    SALE_ENDED("판매 종료"),
    DISCONTINUED("단종");

    private final String displayName;

    ProductStatus(final String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
