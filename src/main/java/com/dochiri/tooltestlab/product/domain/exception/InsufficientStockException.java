package com.dochiri.tooltestlab.product.domain.exception;

import static java.util.Objects.requireNonNull;

public final class InsufficientStockException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductDomainErrorCode errorCode;
    private final int currentStockField;
    private final int requestedField;

    private InsufficientStockException(final ProductDomainErrorCode errorCode,
                                       final int currentStock,
                                       final int requested) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.currentStockField = currentStock;
        this.requestedField = requested;
    }

    public static InsufficientStockException lowStock(final int currentStock, final int requested) {
        return new InsufficientStockException(ProductDomainErrorCode.LOW_STOCK, currentStock, requested);
    }

    public ProductDomainErrorCode code() {
        return errorCode;
    }

    public int currentStock() {
        return currentStockField;
    }

    public int requested() {
        return requestedField;
    }
}
