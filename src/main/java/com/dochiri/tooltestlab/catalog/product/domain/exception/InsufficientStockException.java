package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class InsufficientStockException extends DomainException {

    public InsufficientStockException(final int currentStock, final int requestedQuantity) {
        super(
                ProductDomainErrorCode.STOCK_INSUFFICIENT,
                "상품 재고가 부족합니다. currentStock=%d, requestedQuantity=%d"
                        .formatted(currentStock, requestedQuantity),
                Map.of(
                        "currentStock", currentStock,
                        "requestedQuantity", requestedQuantity
                )
        );
    }

}
