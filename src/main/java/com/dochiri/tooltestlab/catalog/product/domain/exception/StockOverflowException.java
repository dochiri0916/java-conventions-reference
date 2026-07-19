package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class StockOverflowException extends DomainException {

    public StockOverflowException(final int currentStock, final int requestedQuantity) {
        super(
                ProductDomainErrorCode.STOCK_OVERFLOW,
                "재고가 허용 범위를 초과합니다. currentStock=%d, requestedQuantity=%d"
                        .formatted(currentStock, requestedQuantity),
                Map.of(
                        "currentStock", currentStock,
                        "requestedQuantity", requestedQuantity
                )
        );
    }

}
