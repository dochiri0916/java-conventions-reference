package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.product.domain.model.ProductId;

import static java.util.Objects.requireNonNull;

public record Cart(
        CartId id,
        MemberId memberId,
        CartItems items
) {

    public Cart {
        requireNonNull(id, "장바구니 ID는 null일 수 없습니다.");
        requireNonNull(memberId, "회원 ID는 null일 수 없습니다.");
        requireNonNull(items, "장바구니 아이템 목록은 null일 수 없습니다.");
    }

    public static Cart create(MemberId memberId) {
        return new Cart(CartId.generate(), memberId, CartItems.empty());
    }

    public Cart addItem(ProductId productId, String productName, int quantity, Money unitPrice) {
        requireNonNull(productId, "상품 ID는 null일 수 없습니다.");
        requireNonNull(productName, "상품명은 null일 수 없습니다.");
        requireNonNull(unitPrice, "단가는 null일 수 없습니다.");

        return items.findByProductId(productId)
                .map(existing -> {
                    CartItem increased = existing.increaseQuantity(quantity);
                    return new Cart(id, memberId, items.replace(existing, increased));
                })
                .orElseGet(() -> {
                    CartItem newItem = new CartItem(productId, productName, quantity, unitPrice);
                    return new Cart(id, memberId, items.add(newItem));
                });
    }

    public Cart removeItem(ProductId productId) {
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
    public boolean equals(Object other) {
        return this == other
                || other instanceof Cart cart && id.equals(cart.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
