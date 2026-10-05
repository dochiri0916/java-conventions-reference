package com.dochiri.tooltestlab.cart.domain;

import com.dochiri.tooltestlab.cart.domain.vo.CartId;
import com.dochiri.tooltestlab.cart.domain.event.CartChanged;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartRevisionException;
import com.dochiri.tooltestlab.cart.domain.exception.ProductNameRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.ProductReferenceIdRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.QuantityRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.UnitPriceRequiredException;
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
    private final long revision;

    private Cart(final CartId id, final MemberReferenceId memberId, final CartItems items, final long revision) {
        this.id = requireNonNull(id);
        this.memberId = requireNonNull(memberId);
        this.items = requireNonNull(items);
        if (revision < 0) {
            throw new InvalidCartRevisionException();
        }
        this.revision = revision;
    }

    public static Cart create(final MemberReferenceId memberId) {
        return new Cart(CartId.generate(), memberId,
                CartItems.empty(), 0);
    }

    public static Cart restore(final CartId id, final MemberReferenceId memberId, final CartItems items) {
        return restore(id, memberId, items, 0);
    }

    public static Cart restore(final CartId id, final MemberReferenceId memberId,
                               final CartItems items, final long revision) {
        return new Cart(id, memberId, items, revision);
    }

    public Cart addItem(
            final ProductReferenceId productId, final ProductName productName, final Quantity quantity, final Money unitPrice) {
        requireItemValues(productId, productName, quantity, unitPrice);
        return items.findByProductId(productId)
                .map(existing -> new Cart(id, memberId,
                items.replace(existing,
                existing.increaseQuantity(quantity)), revision + 1))
                .orElseGet(() -> new Cart(id, memberId,
                items.add(
                        CartItem.of(productId, productName, quantity, unitPrice)), revision + 1));
    }

    private static void requireItemValues(
            final ProductReferenceId productId, final ProductName productName,
            final Quantity quantity, final Money unitPrice) {
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

    public Cart removeItem(final ProductReferenceId productId) {
        return new Cart(id, memberId, items.remove(productId), revision + 1);
    }

    public Cart clear() {
        return new Cart(id, memberId, CartItems.empty(), revision + 1);
    }

    public long revision() {
        return revision;
    }

    public CartChanged changedEvent() {
        return new CartChanged(this);
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
        return this == other || other instanceof Cart cart && id.equals(
                cart.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
