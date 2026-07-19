package com.dochiri.tooltestlab.catalog.product.application.port.out;

import com.dochiri.tooltestlab.catalog.product.domain.model.Product;

import java.util.List;

public record ProductPage(
        List<Product> products,
        long totalCount
) {
    public ProductPage {
        products = List.copyOf(products);
    }
}
