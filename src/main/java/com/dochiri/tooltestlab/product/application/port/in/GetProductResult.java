package com.dochiri.tooltestlab.product.application.port.in;

import java.util.List;

public record GetProductResult(
        String publicId, String name, String description, long priceAmount,
        int stock, String status, List<String> categoryNames) {

    public GetProductResult {
        categoryNames = List.copyOf(categoryNames);
    }
}
