package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.order.domain.exception.InvalidQuantityException;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.product.domain.exception.InvalidMoneyException;
import com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException;
import com.dochiri.tooltestlab.product.domain.exception.InvalidProductNameException;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.ProductName;
import com.dochiri.tooltestlab.product.domain.model.Money;

public record CartItem(ProductId productId, ProductName productName, Quantity quantity, Money unitPrice) {

    public CartItem {
        if (productId == null) {
            throw InvalidProductIdException.required();
        }
        if (productName == null) {
            throw InvalidProductNameException.required();
        }
        if (quantity == null) {
            throw InvalidQuantityException.required();
        }
        if (unitPrice == null) {
            throw InvalidMoneyException.required();
        }
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
