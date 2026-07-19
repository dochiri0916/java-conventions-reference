package com.dochiri.tooltestlab.catalog.product.domain.model;

import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;

import java.util.Set;

import static java.util.Objects.requireNonNull;

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
        this.id = requireNonNull(id, "상품 ID는 null일 수 없습니다.");
        this.name = requireNonNull(name, "상품명은 null일 수 없습니다.");
        this.description = requireNonNull(description, "상품 설명은 null일 수 없습니다.");
        this.price = requireNonNull(price, "상품 가격은 null일 수 없습니다.");
        this.stock = requireNonNull(stock, "상품 재고는 null일 수 없습니다.");
        this.status = requireNonNull(status, "상품 상태는 null일 수 없습니다.");
        this.categoryIds = Set.copyOf(requireNonNull(categoryIds, "카테고리 ID 목록은 null일 수 없습니다."));
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
