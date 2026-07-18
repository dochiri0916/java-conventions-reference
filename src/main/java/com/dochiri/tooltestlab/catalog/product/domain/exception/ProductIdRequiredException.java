package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

public final class ProductIdRequiredException extends DomainException {

    public ProductIdRequiredException() {
        super(ProductDomainErrorCode.PRODUCT_ID_REQUIRED);
    }

}
