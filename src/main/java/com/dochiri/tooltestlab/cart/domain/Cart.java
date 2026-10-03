package com.dochiri.tooltestlab.cart.domain;

import com.dochiri.tooltestlab.cart.domain.vo.CartId;
import com.dochiri.tooltestlab.cart.domain.vo.CartItem;
import com.dochiri.tooltestlab.cart.domain.vo.CartItems;
import com.dochiri.tooltestlab.cart.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.cart.domain.vo.ProductName;
import com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;

import static java.util.Objects.requireNonNull;

public final class Cart {

    private final CartId id;
    private final MemberReferenceId memberId;
    private final CartItems items;

    private Cart(final CartId id, final MemberReferenceId memberId, final CartItems items) {
        this.id = requireNonNull(id);
        this.memberId = requireNonNull(memberId);
        this.items = requireNonNull(items);
    }

    public static Cart create(final MemberReferenceId memberId) {
        return new Cart(CartId.generate(), memberId, CartItems.empty());
    }

    public static Cart restore(final CartId id, final MemberReferenceId memberId, final CartItems items) {
        return new Cart(id, memberId, items);
    }

    public Cart addItem(
            final ProductReferenceId productId, final ProductName productName, final Quantity quantity, final Money unitPrice) {
        requireNonNull(productId);
        requireNonNull(productName);
        requireNonNull(quantity);
        requireNonNull(unitPrice);
        return items.findByProductId(productId)
                .map(existing -> new Cart(id, memberId, items.replace(existing, existing.increaseQuantity(quantity.value()))))
                .orElseGet(() -> new Cart(id, memberId, items.add(CartItem.of(productId, productName, quantity, unitPrice))));
    }

    public Cart removeItem(final ProductReferenceId productId) {
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

    public CartId id() {
        return id;
    }

    public MemberReferenceId memberId() {
        return memberId;
    }

    public CartItems items() {
        return items;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof Cart cart && id.equals(cart.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
