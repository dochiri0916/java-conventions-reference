package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.model.*;

public final class ProductFixture {

    private static final String DEFAULT_NAME = "노트북";
    private static final String DEFAULT_DESCRIPTION = "고성능 노트북";
    private static final int DEFAULT_PRICE = 1_500_000;
    private static final int DEFAULT_STOCK = 10;

    private ProductFixture() {
    }

    public static Product product() {
        return Product.create(
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                DEFAULT_PRICE,
                DEFAULT_STOCK
        );
    }

    public static Product productWithStock(final int stock) {
        return Product.create(
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                DEFAULT_PRICE,
                stock
        );
    }

    public static Product productWithStatus(final ProductStatus status) {
        return product(DEFAULT_STOCK, status);
    }

    public static Product product(
            final int stock,
            final ProductStatus status
    ) {
        return Product.restore(
                ProductId.generate(),
                new ProductName(DEFAULT_NAME),
                new ProductDescription(DEFAULT_DESCRIPTION),
                new Money(DEFAULT_PRICE),
                new Stock(stock),
                status
        );
    }

}
