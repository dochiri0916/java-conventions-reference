package com.dochiri.tooltestlab.catalog.product.domain.model;

import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;
import com.dochiri.tooltestlab.global.domain.Money;

import java.util.Set;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductCategoryIdsRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductDescriptionRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductIdRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductNameRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductPriceRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductStatusRequiredException;
import com.dochiri.tooltestlab.catalog.product.domain.exception.ProductStockRequiredException;

public final class Product {

    private final ProductId id;
    private final ProductName name;
    private final ProductDescription description;
    private final Money price;
    private final Stock stock;
    private final ProductStatus status;
    private final Set<CategoryId> categoryIds;

    private Product(
            final ProductId id,
            final ProductName name,
            final ProductDescription description,
            final Money price,
            final Stock stock,
            final ProductStatus status,
            final Set<CategoryId> categoryIds
    ) {
        this.id = validated(id, new ProductIdRequiredException());
        this.name = validated(name, new ProductNameRequiredException());
        this.description = validated(description, new ProductDescriptionRequiredException());
        this.price = validated(price, new ProductPriceRequiredException());
        this.stock = validated(stock, new ProductStockRequiredException());
        this.status = validated(status, new ProductStatusRequiredException());
        this.categoryIds = Set.copyOf(validated(categoryIds, new ProductCategoryIdsRequiredException()));
    }

    public static Product create(
            final String name,
            final String description,
            final long price,
            final int stock,
            final Set<CategoryId> categoryIds
    ) {
        return new Product(
                ProductId.generate(),
                new ProductName(name),
                new ProductDescription(description),
                new Money(price),
                new Stock(stock),
                ProductStatus.PREPARING,
                categoryIds
        );
    }

    public static Product create(
            final String name,
            final String description,
            final long price,
            final int stock
    ) {
        return create(name, description, price, stock, Set.of());
    }

    public static Product restore(
            final ProductId id,
            final ProductName name,
            final ProductDescription description,
            final Money price,
            final Stock stock,
            final ProductStatus status,
            final Set<CategoryId> categoryIds
    ) {
        return new Product(
                id,
                name,
                description,
                price,
                stock,
                status,
                categoryIds
        );
    }

    public static Product restore(
            final ProductId id,
            final ProductName name,
            final ProductDescription description,
            final Money price,
            final Stock stock,
            final ProductStatus status
    ) {
        return restore(id, name, description, price, stock, status, Set.of());
    }

    public Product replaceCategories(final Set<CategoryId> categoryIds) {
        return copy(
                stock,
                status,
                categoryIds
        );
    }

    public Product increaseStock(final int quantity) {
        return copy(
                stock.increase(quantity),
                status,
                categoryIds
        );
    }

    public Product decreaseStock(final int quantity) {
        return copy(
                stock.decrease(quantity),
                status,
                categoryIds
        );
    }

    public Product startSale() {
        return changeStatus(status.startSale());
    }

    public Product endSale() {
        return changeStatus(status.endSale());
    }

    public Product discontinue() {
        return changeStatus(status.discontinue());
    }

    public StockStatus stockStatus() {
        return stock.hasStock()
                ? StockStatus.IN_STOCK
                : StockStatus.SOLD_OUT;
    }

    public boolean isPurchasable() {
        return status == ProductStatus.ON_SALE && stock.hasStock();
    }

    public ProductId id() {
        return id;
    }

    public ProductName name() {
        return name;
    }

    public ProductDescription description() {
        return description;
    }

    public Money price() {
        return price;
    }

    public Stock stock() {
        return stock;
    }

    public ProductStatus status() {
        return status;
    }

    public Set<CategoryId> categoryIds() {
        return categoryIds;
    }

    private Product copy(
            final Stock stock,
            final ProductStatus status,
            final Set<CategoryId> categoryIds
    ) {
        return new Product(
                id,
                name,
                description,
                price,
                stock,
                status,
                categoryIds
        );
    }

    private Product changeStatus(final ProductStatus changedStatus) {
        if (status == changedStatus) {
            return this;
        }

        return copy(
                stock,
                changedStatus,
                categoryIds
        );
    }

    private static <T> T validated(final T value, final RuntimeException exception) {
        if (value == null) {
            throw exception;
        }
        return value;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other
                || other instanceof Product product
                && id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
