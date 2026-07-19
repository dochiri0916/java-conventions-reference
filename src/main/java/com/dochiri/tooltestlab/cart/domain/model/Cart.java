package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartIdException;
import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.exception.InvalidQuantityException;
import com.dochiri.tooltestlab.order.domain.model.Quantity;

public record Cart(
        CartId id,
        MemberId memberId,
        CartItems items
) {

    public Cart {
        if (id == null) {
            throw InvalidCartIdException.required();
        }
        if (memberId == null) {
            throw InvalidMemberIdException.required();
        }
        if (items == null) {
            throw InvalidCartException.required();
        }
    }

    public static Cart create(final MemberId memberId) {
        return new Cart(CartId.generate(), memberId, CartItems.empty());
    }

    public Cart addItem(
            final ProductId productId, final ProductName productName, final Quantity quantity, final Money unitPrice) {
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

        return items.findByProductId(productId)
                .map(existing -> {
                    final CartItem increased = existing.increaseQuantity(quantity.value());
                    return new Cart(id, memberId, items.replace(existing, increased));
                })
                .orElseGet(() -> {
                    final CartItem newItem = new CartItem(productId, productName, quantity, unitPrice);
                    return new Cart(id, memberId, items.add(newItem));
                });
    }

    public Cart removeItem(final ProductId productId) {
        return new Cart(id, memberId, items.remove(productId));
    }

    public Cart clear() {
        return new Cart(id, memberId, CartItems.empty());
    }

    public Money totalAmount() {
        return items.totalAmount();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other
                || other instanceof Cart cart && id.equals(cart.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
