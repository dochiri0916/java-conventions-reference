package com.dochiri.tooltestlab.order.domain.model;

import static java.util.Objects.requireNonNull;

/**
 * 주문 상품. 주문 시점의 상품명과 가격을 스냅샷으로 저장.
 */
public record OrderLine(String productPublicId, String productName, int quantity, long unitPrice) {

    public OrderLine {
        requireNonNull(productPublicId, "상품 ID는 null일 수 없습니다.");
        requireNonNull(productName, "상품명은 null일 수 없습니다.");

        productName = productName.strip();

        if (productPublicId.isBlank()) throw new IllegalArgumentException("상품 ID는 빈 값일 수 없습니다.");
        if (productName.isBlank()) throw new IllegalArgumentException("상품명은 빈 값일 수 없습니다.");
        if (quantity <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다. 입력값: " + quantity);
        if (unitPrice < 0) throw new IllegalArgumentException("단가는 0 이상이어야 합니다. 입력값: " + unitPrice);
    }

    public long totalPrice() {
        return unitPrice * quantity;
    }
}
