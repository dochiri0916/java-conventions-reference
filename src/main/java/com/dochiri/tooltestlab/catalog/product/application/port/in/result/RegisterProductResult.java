package com.dochiri.tooltestlab.catalog.product.application.port.in.result;

public record RegisterProductResult(
        String publicId,
        String name,
        long priceAmount,
        int stock,
        String status,
        CategoryNameItems categoryNames
) {
}
