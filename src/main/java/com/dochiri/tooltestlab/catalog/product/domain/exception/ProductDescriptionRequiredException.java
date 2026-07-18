package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.DomainException;

public final class ProductDescriptionRequiredException extends DomainException {

    public ProductDescriptionRequiredException() {
        super(ProductDomainErrorCode.PRODUCT_DESCRIPTION_REQUIRED);
    }

}
