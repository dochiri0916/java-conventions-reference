package com.dochiri.tooltestlab.catalog.product.application.port.in.result;

import com.dochiri.tooltestlab.catalog.product.domain.model.ProductStatus;

import java.util.List;

public record GetProductResult(
        String productId,
        String name,
        String description,
        long priceAmount,
        int stock,
        ProductStatus status,
        List<String> categoryNames
) {
    public GetProductResult {
        categoryNames = List.copyOf(categoryNames);
    }
}