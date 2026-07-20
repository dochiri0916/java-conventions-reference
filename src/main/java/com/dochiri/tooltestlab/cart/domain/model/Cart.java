package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.cart.domain.exception.CartDomainException;
import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;

public final class Cart {

    private final CartId id;
    private final MemberId memberId;
    private final CartItems items;

    private Cart(final CartId id, final MemberId memberId, final CartItems items) {
        this.id = validated(id, CartDomainException.invalidCartId());
        this.memberId = java.util.Objects.requireNonNull(memberId, "회원 ID는 null일 수 없습니다.");
        this.items = validated(items, CartDomainException.cartRequired());
    }

    public static Cart create(final MemberId memberId) {
        return new Cart(CartId.generate(), memberId, CartItems.empty());
    }

    public static Cart restore(final CartId id, final MemberId memberId, final CartItems items) {
        return new Cart(id, memberId, items);
    }

    public Cart addItem(
            final ProductId productId, final ProductName productName, final Quantity quantity, final Money unitPrice) {
        validated(productId, CartDomainException.productIdRequired());
        validated(productName, CartDomainException.productNameRequired());
        validated(quantity, CartDomainException.quantityRequired());
        validated(unitPrice, CartDomainException.unitPriceRequired());
        return items.findByProductId(productId)
                .map(existing -> new Cart(id, memberId, items.replace(existing, existing.increaseQuantity(quantity.value()))))
                .orElseGet(() -> new Cart(id, memberId, items.add(new CartItem(productId, productName, quantity, unitPrice))));
    }

    public Cart removeItem(final ProductId productId) { return new Cart(id, memberId, items.remove(productId)); }
    public Cart clear() { return new Cart(id, memberId, CartItems.empty()); }
    public Money totalAmount() { return items.totalAmount(); }
    public boolean isEmpty() { return items.isEmpty(); }
    public CartId id() { return id; }
    public MemberId memberId() { return memberId; }
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
