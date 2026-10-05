package com.dochiri.tooltestlab.catalog.product.application.port.in.result;

public record ListProductsResult(
        ProductSummaries products,
        int page,
        int size,
        long totalCount
) {

    public record ProductSummary(
            String publicId,
            String name,
            long priceAmount,
            int stock,
            String status,
            CategoryNameItems categoryNames
    ) {
    }
}
