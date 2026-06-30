package com.dochiri.tooltestlab.product.application.exception;

import static java.util.Objects.requireNonNull;

public final class ProductNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ProductErrorCode errorCode;
    private final String productPublicId;

    private ProductNotFoundException(final ProductErrorCode errorCode, final String productPublicId) {
        super(errorCode.name());
        this.errorCode = requireNonNull(errorCode);
        this.productPublicId = requireNonNull(productPublicId);
    }

    public static ProductNotFoundException productNotFound(final String productPublicId) {
        return new ProductNotFoundException(ProductErrorCode.PRODUCT_NOT_FOUND, productPublicId);
    }

    public ProductErrorCode code() {
        return errorCode;
    }

    public String productPublicId() {
        return productPublicId;
    }
}
