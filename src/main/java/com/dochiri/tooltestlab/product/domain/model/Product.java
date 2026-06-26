package com.dochiri.tooltestlab.product.domain.model;


import java.util.Objects;
import java.util.UUID;

/**
 * 상품 Aggregate Root.
 */
public record Product(
        String publicId,
        String name,
        String description,
        Money price,
        int stock,
        ProductStatus status) {

    public Product {
        Objects.requireNonNull(publicId, "publicId는 null일 수 없습니다.");
        Objects.requireNonNull(name, "상품명은 null일 수 없습니다.");
        Objects.requireNonNull(description, "상품 설명은 null일 수 없습니다.");
        Objects.requireNonNull(price, "가격은 null일 수 없습니다.");
        Objects.requireNonNull(status, "상품 상태는 null일 수 없습니다.");

        name = name.strip();
        description = description.strip();

        if (publicId.isBlank()) throw new IllegalArgumentException("publicId는 빈 값일 수 없습니다.");
        if (name.isBlank()) throw new IllegalArgumentException("상품명은 빈 값일 수 없습니다.");
        if (stock < 0) throw new IllegalArgumentException("재고는 0 이상이어야 합니다. 입력값: " + stock);
    }

    public static Product create(String name, String description, Money price, int stock) {
        ProductStatus status = stock > 0 ? ProductStatus.ON_SALE : ProductStatus.SOLD_OUT;
        return new Product(
                UUID.randomUUID().toString().replace("-", ""),
                name,
                description != null ? description : "",
                price,
                stock,
                status);
    }

    /**
     * 재고 감소. 재고가 부족하면 예외.
     */
    public Product decreaseStock(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("감소 수량은 1 이상이어야 합니다. 입력값: " + quantity);
        if (this.stock < quantity) throw new IllegalStateException("재고가 부족합니다. 현재 재고: " + this.stock + ", 요청 수량: " + quantity);

        int newStock = this.stock - quantity;
        ProductStatus newStatus = newStock == 0 ? ProductStatus.SOLD_OUT : this.status;
        return new Product(publicId, name, description, price, newStock, newStatus);
    }

    /**
     * 재고 증가.
     */
    public Product increaseStock(int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("증가 수량은 1 이상이어야 합니다. 입력값: " + quantity);
        int newStock = this.stock + quantity;
        return new Product(publicId, name, description, price, newStock, ProductStatus.ON_SALE);
    }

    /**
     * 판매 중단.
     */
    public Product discontinue() {
        return new Product(publicId, name, description, price, stock, ProductStatus.DISCONTINUED);
    }
}
