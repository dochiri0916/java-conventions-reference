package com.dochiri.tooltestlab.product.application.exception;

import static java.util.Objects.requireNonNull;

public final class ProductNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductErrorCode errorCode;
    private final String missingProductId;

    private ProductNotFoundException(final ProductErrorCode errorCode, final String missingProductId) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.missingProductId = requireNonNull(missingProductId);
    }

    public static ProductNotFoundException productNotFound(final String productId) {
        return new ProductNotFoundException(ProductErrorCode.PRODUCT_NOT_FOUND, productId);
    }

    public ProductErrorCode code() {
        return errorCode;
    }

    public String productId() {
        return missingProductId;
    }
}
