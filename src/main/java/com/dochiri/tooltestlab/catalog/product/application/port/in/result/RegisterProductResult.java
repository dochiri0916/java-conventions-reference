package com.dochiri.tooltestlab.catalog.product.application.port.in.result;

import com.dochiri.tooltestlab.catalog.product.domain.model.ProductStatus;

import java.util.List;

public record RegisterProductResult(
        String publicId,
        String name,
        long priceAmount,
        int stock,
        ProductStatus status,
        List<String> categoryNames
) {
    public RegisterProductResult {
        categoryNames = List.copyOf(categoryNames);
    }
}
