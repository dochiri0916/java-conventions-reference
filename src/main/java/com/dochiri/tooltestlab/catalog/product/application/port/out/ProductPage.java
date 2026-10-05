package com.dochiri.tooltestlab.catalog.product.application.port.out;

public record ProductPage(
        Products products,
        long totalCount
) {
}
