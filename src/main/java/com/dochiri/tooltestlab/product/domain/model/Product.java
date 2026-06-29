package com.dochiri.tooltestlab.product.domain.model;

import com.dochiri.tooltestlab.shared.model.Money;

import static java.util.Objects.requireNonNull;

public record Product(
        ProductId id,
        ProductName name,
        ProductDescription description,
        Money price,
        Stock stock,
        ProductStatus status
) {

    public Product {
        requireNonNull(id, "상품 ID는 null일 수 없습니다.");
        requireNonNull(name, "상품명은 null일 수 없습니다.");
        requireNonNull(description, "상품 설명은 null일 수 없습니다.");
        requireNonNull(price, "가격은 null일 수 없습니다.");
        requireNonNull(stock, "재고는 null일 수 없습니다.");
        requireNonNull(status, "상품 상태는 null일 수 없습니다.");
    }

    public static Product create(final String name, final String description, final Money price, final int stock) {
        final Stock stockVo = Stock.from(stock);
        final ProductStatus status = stockVo.isPositive() ? ProductStatus.ON_SALE : ProductStatus.SOLD_OUT;
        return new Product(
                ProductId.generate(),
                new ProductName(name),
                new ProductDescription(description != null ? description : ""),
                price,
                stockVo,
                status);
    }

    public Product decreaseStock(final int quantity) {
        final Stock newStock = this.stock.decrease(quantity);
        final ProductStatus newStatus = newStock.isZero() ? ProductStatus.SOLD_OUT : this.status;
        return new Product(id, name, description, price, newStock, newStatus);
    }

    public Product increaseStock(final int quantity) {
        final Stock newStock = this.stock.increase(quantity);
        return new Product(id, name, description, price, newStock, ProductStatus.ON_SALE);
    }

    public Product discontinue() {
        return new Product(id, name, description, price, stock, ProductStatus.DISCONTINUED);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other
                || other instanceof Product product && id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
