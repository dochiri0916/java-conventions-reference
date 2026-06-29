package com.dochiri.tooltestlab.product.application.result;

import java.util.List;

public record ListProductsResult(
        List<ProductSummary> products, int page, int size, long totalCount) {

    public record ProductSummary(
            String publicId, String name, long priceAmount, int stock, String status,
            List<String> categoryNames) {
    }
}
