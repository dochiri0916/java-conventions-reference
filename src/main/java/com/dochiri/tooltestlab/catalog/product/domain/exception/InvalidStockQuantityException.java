package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class InvalidStockQuantityException extends DomainException {

    public InvalidStockQuantityException(final int quantity) {
        super(
                ProductDomainErrorCode.STOCK_QUANTITY_INVALID,
                "재고 변경 수량은 1 이상이어야 합니다. quantity=%d"
                        .formatted(quantity),
                Map.of("quantity", quantity)
        );
    }

}
