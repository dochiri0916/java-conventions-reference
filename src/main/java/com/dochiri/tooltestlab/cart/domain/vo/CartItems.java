package com.dochiri.tooltestlab.cart.domain.vo;

import com.dochiri.tooltestlab.cart.domain.exception.CartItemRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.cart.domain.exception.ProductReferenceIdRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.TooManyCartItemsException;
import com.dochiri.tooltestlab.global.domain.vo.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CartItems {

    private static final int MAX_ITEM_COUNT = 100;

    private final List<CartItem> values;

    private CartItems(final List<CartItem> values) {
        if (values == null) {
            throw new CartItemRequiredException();
        }

        if (values.size() > MAX_ITEM_COUNT) {
            throw new TooManyCartItemsException();
        }

        if (values.stream().anyMatch(v -> v == null)) {
            throw new InvalidCartItemException();
        }

        this.values =
                List.copyOf(values);
    }

    public static CartItems empty() {
        return new CartItems(List.of());
    }

    public static CartItems of(final List<CartItem> items) {
        return new CartItems(items);
    }

    public Money totalAmount() {
        return values.stream()
                .map(CartItem::totalPrice)
                .reduce(Money.ZERO, Money::add);
    }

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public boolean containsProduct(final ProductReferenceId productId) {
        if (productId == null) {
            throw new ProductReferenceIdRequiredException();
        }
        return values.stream().anyMatch(item -> item.productId().equals(productId));
    }

    public Optional<CartItem> findByProductId(final ProductReferenceId productId) {
        if (productId == null) {
            throw new ProductReferenceIdRequiredException();
        }
        return values.stream()
                .filter(item -> item.productId().equals(productId))
                .findFirst();
    }

    public CartItems add(final CartItem item) {
        if (item == null) {
            throw new CartItemRequiredException();
        }
        final List<CartItem> changed = new ArrayList<>(values);
        changed.add(item);
        return new CartItems(changed);
    }

    public CartItems replace(final CartItem oldItem, final CartItem newItem) {
        if (oldItem == null || newItem == null) {
            throw new CartItemRequiredException();
        }
        final List<CartItem> changed = new ArrayList<>(values);
        final int idx = changed.indexOf(oldItem);
        if (idx < 0) {
            throw new InvalidCartItemException();
        }
        changed.set(idx, newItem);
        return new CartItems(changed);
    }

    public CartItems remove(final ProductReferenceId productId) {
        if (productId == null) {
            throw new ProductReferenceIdRequiredException();
        }

        final boolean exists = values.stream()
                .anyMatch(item -> item.productId().equals(productId));
        if (!exists) {
            throw new InvalidCartItemException();
        }

        final List<CartItem> changed = values.stream()
                .filter(item -> !item.productId().equals(productId))
                .toList();
        return new CartItems(changed);
    }

    public List<CartItem> values() {
        return values;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof final CartItems other)) {
            return false;
        }
        return values.equals(
                other.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public String toString() {
        return "CartItems[values=" + values + "]";
    }

}
