package com.dochiri.tooltestlab.catalog.product.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.exception.StatusTransitionNotAllowedException;

public enum ProductStatus {
    PREPARING("판매 준비"),
    ON_SALE("판매 중"),
    SALE_ENDED("판매 종료"),
    DISCONTINUED("단종");

    private final String displayName;

    ProductStatus(final String displayName) {
        this.displayName = displayName;
    }

    public ProductStatus startSale() {
        if (this != PREPARING && this != SALE_ENDED) {
            throw new StatusTransitionNotAllowedException(
                    this,
                    ON_SALE
            );
        }

        return ON_SALE;
    }

    public ProductStatus endSale() {
        if (this != ON_SALE) {
            throw new StatusTransitionNotAllowedException(
                    this,
                    SALE_ENDED
            );
        }

        return SALE_ENDED;
    }

    public ProductStatus discontinue() {
        return DISCONTINUED;
    }

    public String displayName() {
        return displayName;
    }
}
