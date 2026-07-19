package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

import java.util.Map;

public final class NegativeStockException extends DomainException {

    public NegativeStockException(final int stock) {
        super(
                ProductDomainErrorCode.STOCK_NEGATIVE,
                "재고는 0 이상이어야 합니다. stock=%d"
                        .formatted(stock),
                Map.of("stock", stock)
        );
    }

}
