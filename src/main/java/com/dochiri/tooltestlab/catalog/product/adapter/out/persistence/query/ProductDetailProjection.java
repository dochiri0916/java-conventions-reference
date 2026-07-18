package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence.query;

import com.dochiri.tooltestlab.catalog.product.domain.model.ProductStatus;

public record ProductDetailProjection(
        String publicId,
        String name,
        String description,
        long priceAmount,
        int stock,
        ProductStatus status
) {
}
