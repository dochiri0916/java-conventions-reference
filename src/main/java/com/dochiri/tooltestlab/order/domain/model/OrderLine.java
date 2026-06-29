package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.shared.model.Money;

import static java.util.Objects.requireNonNull;

public record OrderLine(
        ProductId productId,
        ProductName productName,
        Quantity quantity,
        Money unitPrice
) {

    public OrderLine {
        requireNonNull(productId, "상품 ID는 null일 수 없습니다.");
        requireNonNull(productName, "상품명은 null일 수 없습니다.");
        requireNonNull(quantity, "수량은 null일 수 없습니다.");
        requireNonNull(unitPrice, "단가는 null일 수 없습니다.");
    }

    public Money totalPrice() {
        return unitPrice.multiply(quantity.value());
    }
}
