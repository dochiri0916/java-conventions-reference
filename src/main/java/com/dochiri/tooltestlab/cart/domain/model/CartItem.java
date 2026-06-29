package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.product.domain.model.ProductId;

import static java.util.Objects.requireNonNull;

/**
 * 장바구니 상품 Value Object. 장바구니에 담긴 상품의 스냅샷.
 */
public record CartItem(ProductId productId, String productName, int quantity, Money unitPrice) {

    public CartItem {
        requireNonNull(productId, "상품 ID는 null일 수 없습니다.");
        requireNonNull(productName, "상품명은 null일 수 없습니다.");
        requireNonNull(unitPrice, "단가는 null일 수 없습니다.");

        productName = productName.strip();

        if (productName.isBlank()) throw new IllegalArgumentException("상품명은 빈 값일 수 없습니다.");
        if (quantity <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다. 입력값: " + quantity);
    }

    public Money totalPrice() {
        return unitPrice.multiply(quantity);
    }

    public CartItem increaseQuantity(int delta) {
        if (delta <= 0) throw new IllegalArgumentException("증가 수량은 1 이상이어야 합니다. 입력값: " + delta);
        return new CartItem(productId, productName, quantity + delta, unitPrice);
    }
}
