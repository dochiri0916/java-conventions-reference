package com.dochiri.tooltestlab.catalog.product.domain;

import com.dochiri.tooltestlab.catalog.product.domain.event.ProductChanged;
import com.dochiri.tooltestlab.catalog.product.domain.vo.CategoryIds;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductDescription;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductName;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductStatus;
import com.dochiri.tooltestlab.catalog.product.domain.vo.Stock;
import com.dochiri.tooltestlab.catalog.product.domain.vo.StockStatus;
import com.dochiri.tooltestlab.global.domain.vo.Money;

import static java.util.Objects.requireNonNull;

public final class Product {

    private final ProductId id;
    private final ProductName name;
    private final ProductDescription description;
    private final Money price;
    private final Stock stock;
    private final ProductStatus status;
    private final CategoryIds categoryIds;

    private Product(
            final ProductId id,
            final ProductName name,
            final ProductDescription description,
            final Money price,
            final Stock stock,
            final ProductStatus status,
            final CategoryIds categoryIds
    ) {
        this.id = requireNonNull(id);
        this.name = requireNonNull(name);
        this.description = requireNonNull(description);
        this.price = requireNonNull(price);
        this.stock = requireNonNull(stock);
        this.status = requireNonNull(status);
        this.categoryIds = requireNonNull(categoryIds);
    }

    public static Product create(
            final ProductName name,
            final ProductDescription description,
            final Money price,
            final Stock stock,
            final CategoryIds categoryIds
    ) {
        return new Product(
                ProductId.generate(),
                name,
                description,
                price,
                stock,
                ProductStatus.PREPARING,
                categoryIds
        );
    }

    public static Product create(
            final ProductName name,
            final ProductDescription description,
            final Money price,
            final Stock stock
    ) {
        return create(name, description, price, stock, CategoryIds.empty());
    }

    public static Product restore(
            final ProductId id,
            final ProductName name,
            final ProductDescription description,
            final Money price,
            final Stock stock,
            final ProductStatus status,
            final CategoryIds categoryIds
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
        return restore(id, name, description, price, stock, status, CategoryIds.empty());
    }

    public Product replaceCategories(final CategoryIds categoryIds) {
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
        return status == ProductStatus.ON_SALE &&
                stock.hasStock();
    }

    public ProductId id() {
        return id;
    }

    public ProductChanged changedEvent() {
        return new ProductChanged(this);
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

    public CategoryIds categoryIds() {
        return categoryIds;
    }

    private Product copy(
            final Stock stock,
            final ProductStatus status,
            final CategoryIds categoryIds
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

    @Override
    public boolean equals(final Object other) {
        return this == other
                || other instanceof Product product
                && id.equals(
                        product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
