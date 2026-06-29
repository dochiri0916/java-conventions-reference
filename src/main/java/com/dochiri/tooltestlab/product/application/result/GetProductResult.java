package com.dochiri.tooltestlab.product.application.result;

import java.util.List;

public record GetProductResult(
        String publicId, String name, String description, long priceAmount,
        int stock, String status, List<String> categoryNames) {
}
