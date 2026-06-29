package com.dochiri.tooltestlab.product.domain.model;


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

    public static Product create(String name, String description, Money price, int stock) {
        Stock stockVo = Stock.of(stock);
        ProductStatus status = stockVo.isPositive() ? ProductStatus.ON_SALE : ProductStatus.SOLD_OUT;
        return new Product(
                ProductId.generate(),
                new ProductName(name),
                new ProductDescription(description != null ? description : ""),
                price,
                stockVo,
                status);
    }

    public Product decreaseStock(int quantity) {
        Stock newStock = this.stock.decrease(quantity);
        ProductStatus newStatus = newStock.isZero() ? ProductStatus.SOLD_OUT : this.status;
        return new Product(id, name, description, price, newStock, newStatus);
    }

    public Product increaseStock(int quantity) {
        Stock newStock = this.stock.increase(quantity);
        return new Product(id, name, description, price, newStock, ProductStatus.ON_SALE);
    }

    public Product discontinue() {
        return new Product(id, name, description, price, stock, ProductStatus.DISCONTINUED);
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || other instanceof Product product && id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
