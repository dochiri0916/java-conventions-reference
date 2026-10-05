package com.dochiri.tooltestlab.catalog.product.application.port.in.result;

public record GetProductResult(
        String publicId,
        String name,
        String description,
        long priceAmount,
        int stock,
        String status,
        CategoryNameItems categoryNames
) {
}
