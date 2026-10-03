package com.dochiri.tooltestlab.cart.domain.vo;

import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.cart.domain.exception.ProductNameRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.ProductReferenceIdRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.QuantityRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.UnitPriceRequiredException;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;

import java.util.Objects;

public final class CartItem {

    private final ProductReferenceId productId;
    private final ProductName productName;
    private final Quantity quantity;
    private final Money unitPrice;

    private CartItem(
            final ProductReferenceId productId,
            final ProductName productName,
            final Quantity quantity,
            final Money unitPrice
    ) {
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
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Money totalPrice() {
        return unitPrice.multiply(quantity.value());
    }

    public CartItem increaseQuantity(final int delta) {
        if (delta <= 0) {
            throw new InvalidCartItemException();
        }
        return new CartItem(productId, productName, Quantity.of(quantity.value() + delta), unitPrice);
    }

    public static CartItem of(
            final ProductReferenceId productId,
            final ProductName productName,
            final Quantity quantity,
            final Money unitPrice
    ) {
        return new CartItem(productId, productName, quantity, unitPrice);
    }

    public ProductReferenceId productId() {
        return productId;
    }

    public ProductName productName() {
        return productName;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Money unitPrice() {
        return unitPrice;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final CartItem other)) {
            return false;
        }
        return productId.equals(other.productId)
                && productName.equals(other.productName)
                && quantity.equals(other.quantity)
                && unitPrice.equals(other.unitPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, quantity, unitPrice);
    }

    @Override
    public String toString() {
        return "CartItem[productId=" + productId + ", productName=" + productName + ", quantity=" + quantity + ", unitPrice=" + unitPrice + "]";
    }

}
