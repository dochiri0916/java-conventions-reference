package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.cart.domain.exception.ProductReferenceIdRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.ProductNameRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.QuantityRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.UnitPriceRequiredException;
import com.dochiri.tooltestlab.global.domain.Money;
import com.dochiri.tooltestlab.global.domain.Quantity;

public record CartItem(ProductReferenceId productId, ProductName productName, Quantity quantity, Money unitPrice) {

    public CartItem {
        if (productId == null) {
            throw new ProductReferenceIdRequiredException();
        }
        if (productName == null) {
            throw new ProductNameRequiredException();
        }
        if (quantity == null) {
            throw new QuantityRequiredException();
        }
        if (unitPrice == null) {
            throw new UnitPriceRequiredException();
        }
    }

    public Money totalPrice() {
        return unitPrice.multiply(quantity.value());
    }

    public CartItem increaseQuantity(final int delta) {
        if (delta <= 0) {
            throw new InvalidCartItemException();
        }
        return new CartItem(productId, productName, new Quantity(quantity.value() + delta), unitPrice);
    }
}
