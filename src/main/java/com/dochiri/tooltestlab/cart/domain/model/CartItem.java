package com.dochiri.tooltestlab.cart.domain.model;

import java.util.Objects;

/**
 * 장바구니 아이템. 담을 당시의 상품명과 가격을 스냅샷으로 저장.
 */
public record CartItem(String productPublicId, String productName, int quantity, long unitPrice) {

    public CartItem {
        Objects.requireNonNull(productPublicId, "상품 ID는 null일 수 없습니다.");
        Objects.requireNonNull(productName, "상품명은 null일 수 없습니다.");

        productName = productName.strip();

        if (productPublicId.isBlank()) throw new IllegalArgumentException("상품 ID는 빈 값일 수 없습니다.");
        if (productName.isBlank()) throw new IllegalArgumentException("상품명은 빈 값일 수 없습니다.");
        if (quantity <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다. 입력값: " + quantity);
        if (unitPrice < 0) throw new IllegalArgumentException("단가는 0 이상이어야 합니다. 입력값: " + unitPrice);
    }

    public long totalPrice() {
        return unitPrice * quantity;
    }

    public CartItem increaseQuantity(int delta) {
        return new CartItem(productPublicId, productName, quantity + delta, unitPrice);
    }
}
