package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

public final class ProductNameRequiredException extends DomainException {

    public ProductNameRequiredException() {
        super(ProductDomainErrorCode.PRODUCT_NAME_REQUIRED);
    }

}
