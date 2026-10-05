package com.dochiri.tooltestlab.catalog.product.domain.event;

import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.Product;

public record ProductChanged(Product product) {

    public ProductChanged {
        if (product == null) {
            throw new ProductRequiredException();
        }
    }

}
