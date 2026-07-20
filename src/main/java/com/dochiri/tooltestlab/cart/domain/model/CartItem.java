package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.CartDomainException;
import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.order.domain.model.Quantity;

public record CartItem(ProductId productId, ProductName productName, Quantity quantity, Money unitPrice) {

    public CartItem {
        if (productId == null) {
            throw CartDomainException.productIdRequired();
        }
        if (productName == null) {
            throw CartDomainException.productNameRequired();
        }
        if (quantity == null) {
            throw CartDomainException.quantityRequired();
        }
        if (unitPrice == null) {
            throw CartDomainException.unitPriceRequired();
        }
    }

    public Money totalPrice() {
        return unitPrice.multiply(quantity.value());
    }

    public CartItem increaseQuantity(final int delta) {
        if (delta <= 0) {
            throw CartDomainException.invalidCartItem("증가 수량은 1 이상이어야 합니다. 입력값: " + delta);
        }
        return new CartItem(productId, productName, new Quantity(quantity.value() + delta), unitPrice);
    }
}
