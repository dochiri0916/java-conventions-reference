package com.dochiri.tooltestlab.catalog.product.application.port.in.result;

import com.dochiri.tooltestlab.catalog.product.domain.model.ProductStatus;

import java.util.List;

public record ListProductsResult(
        List<ProductSummary> products,
        int page,
        int size,
        long totalCount
) {

    public ListProductsResult {
        products = List.copyOf(products);
    }

    public record ProductSummary(
            String publicId,
            String name,
            long priceAmount,
            int stock,
            ProductStatus status,
            List<String> categoryNames
    ) {
        public ProductSummary {
            categoryNames = List.copyOf(categoryNames);
        }
    }
}
