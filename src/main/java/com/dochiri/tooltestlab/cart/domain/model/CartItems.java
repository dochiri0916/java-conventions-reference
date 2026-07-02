package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.CartItemLimitExceededException;
import com.dochiri.tooltestlab.cart.domain.exception.InvalidCartItemException;
import com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.MaxCount;
import com.dochiri.tooltestlab.product.domain.model.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record CartItems(List<CartItem> values) {

    public static final MaxCount MAX_ITEM_COUNT = MaxCount.of(100);

    public CartItems {
        if (values == null) {
            throw InvalidCartItemException.required();
        }

        if (values.size() > MAX_ITEM_COUNT.value()) {
            throw CartItemLimitExceededException.tooManyItems(values.size());
        }

        if (values.stream().anyMatch(v -> v == null)) {
            throw InvalidCartItemException.invalidCartItem("장바구니 상품은 null일 수 없습니다.");
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

    public boolean containsProduct(final ProductId productId) {
        if (productId == null) {
            throw InvalidProductIdException.required();
        }
        return values.stream().anyMatch(item -> item.productId().equals(productId));
    }

    public Optional<CartItem> findByProductId(final ProductId productId) {
        if (productId == null) {
            throw InvalidProductIdException.required();
        }
        return values.stream()
                .filter(item -> item.productId().equals(productId))
                .findFirst();
    }

    public CartItems add(final CartItem item) {
        if (item == null) {
            throw InvalidCartItemException.required();
        }
        final List<CartItem> changed = new ArrayList<>(values);
        changed.add(item);
        return new CartItems(changed);
    }

    public CartItems replace(final CartItem oldItem, final CartItem newItem) {
        if (oldItem == null || newItem == null) {
            throw InvalidCartItemException.required();
        }
        final List<CartItem> changed = new ArrayList<>(values);
        final int idx = changed.indexOf(oldItem);
        if (idx < 0) {
            throw InvalidCartItemException.invalidCartItem("교체 대상 상품이 장바구니에 존재하지 않습니다.");
        }
        changed.set(idx, newItem);
        return new CartItems(changed);
    }

    public CartItems remove(final ProductId productId) {
        if (productId == null) {
            throw InvalidProductIdException.required();
        }

        final boolean exists = values.stream()
                .anyMatch(item -> item.productId().equals(productId));
        if (!exists) {
            throw InvalidCartItemException.invalidCartItem(
                    "해당 상품이 장바구니에 존재하지 않습니다. productId=" + productId.value());
        }

        final List<CartItem> changed = values.stream()
                .filter(item -> !item.productId().equals(productId))
                .toList();
        return new CartItems(changed);
    }
}
