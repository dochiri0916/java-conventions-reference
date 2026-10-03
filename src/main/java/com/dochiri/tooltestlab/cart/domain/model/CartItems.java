package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.CartItemRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.cart.domain.exception.ProductReferenceIdRequiredException;
import com.dochiri.tooltestlab.cart.domain.exception.TooManyCartItemsException;
import com.dochiri.tooltestlab.global.domain.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record CartItems(List<CartItem> values) {

    private static final int MAX_ITEM_COUNT = 100;

    public CartItems {
        if (values == null) {
            throw new CartItemRequiredException();
        }

        if (values.size() > MAX_ITEM_COUNT) {
            throw new TooManyCartItemsException();
        }

        if (values.stream().anyMatch(v -> v == null)) {
            throw new InvalidCartItemException();
        }

        values = List.copyOf(values);
    }

    public static CartItems empty() {
        return new CartItems(List.of());
    }

    public static CartItems from(final List<CartItem> items) {
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
}
