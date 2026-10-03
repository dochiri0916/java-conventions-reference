package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.CartRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartIdException;
import com.dochiri.tooltestlab.cart.domain.exception.MemberReferenceIdRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.ProductReferenceIdRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.ProductNameRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.QuantityRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.UnitPriceRequiredException;
import com.dochiri.tooltestlab.global.domain.Money;
import com.dochiri.tooltestlab.global.domain.Quantity;

public final class Cart {

    private final CartId id;
    private final MemberReferenceId memberId;
    private final CartItems items;

    private Cart(final CartId id, final MemberReferenceId memberId, final CartItems items) {
        this.id = validated(id, new InvalidCartIdException());
        this.memberId = validated(memberId, new MemberReferenceIdRequiredException());
        this.items = validated(items, new CartRequiredException());
    }

    public static Cart create(final MemberReferenceId memberId) {
        return new Cart(CartId.generate(), memberId, CartItems.empty());
    }

    public static Cart restore(final CartId id, final MemberReferenceId memberId, final CartItems items) {
        return new Cart(id, memberId, items);
    }

    public Cart addItem(
            final ProductReferenceId productId, final ProductName productName, final Quantity quantity, final Money unitPrice) {
        validated(productId, new ProductReferenceIdRequiredException());
        validated(productName, new ProductNameRequiredException());
        validated(quantity, new QuantityRequiredException());
        validated(unitPrice, new UnitPriceRequiredException());
        return items.findByProductId(productId)
                .map(existing -> new Cart(id, memberId, items.replace(existing, existing.increaseQuantity(quantity.value()))))
                .orElseGet(() -> new Cart(id, memberId, items.add(new CartItem(productId, productName, quantity, unitPrice))));
    }

    public Cart removeItem(final ProductReferenceId productId) { return new Cart(id, memberId, items.remove(productId)); }
    public Cart clear() { return new Cart(id, memberId, CartItems.empty()); }
    public Money totalAmount() { return items.totalAmount(); }
    public boolean isEmpty() { return items.isEmpty(); }
    public CartId id() { return id; }
    public MemberReferenceId memberId() { return memberId; }
    public CartItems items() { return items; }

    private static <T> T validated(final T value, final RuntimeException exception) {
        if (value == null) {
            throw exception;
        }
        return value;
    }

    @Override public boolean equals(final Object other) {
        return this == other || other instanceof Cart cart && id.equals(cart.id);
    }
    @Override public int hashCode() { return id.hashCode(); }
}
