package com.dochiri.tooltestlab.catalog.product.application.port.in.result;

import java.util.List;

public record RegisterProductResult(
        String publicId,
        String name,
        long priceAmount,
        int stock,
        String status,
        List<String> categoryNames
) {
    public RegisterProductResult {
        categoryNames = List.copyOf(categoryNames);
    }
}
