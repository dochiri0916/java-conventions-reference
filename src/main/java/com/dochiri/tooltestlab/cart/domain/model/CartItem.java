package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.shared.model.Money;

import static java.util.Objects.requireNonNull;

public record CartItem(ProductId productId, ProductName productName, Quantity quantity, Money unitPrice) {

    public CartItem {
        requireNonNull(productId, "상품 ID는 null일 수 없습니다.");
        requireNonNull(productName, "상품명은 null일 수 없습니다.");
        requireNonNull(quantity, "수량은 null일 수 없습니다.");
        requireNonNull(unitPrice, "단가는 null일 수 없습니다.");
    }

    public Money totalPrice() {
        return unitPrice.multiply(quantity.value());
    }

    public CartItem increaseQuantity(final int delta) {
        if (delta <= 0) {
            throw InvalidCartItemException.invalidCartItem("증가 수량은 1 이상이어야 합니다. 입력값: " + delta);
        }
        return new CartItem(productId, productName, new Quantity(quantity.value() + delta), unitPrice);
    }
}
