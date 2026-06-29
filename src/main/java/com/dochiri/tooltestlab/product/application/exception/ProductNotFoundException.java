package com.dochiri.tooltestlab.product.application.exception;

public final class ProductNotFoundException extends ProductApplicationException {

    public ProductNotFoundException(final String productPublicId) {
        super(ProductErrorCode.PRODUCT_NOT_FOUND,
                "해당 상품을 찾을 수 없습니다. productPublicId=" + productPublicId);
    }
}
