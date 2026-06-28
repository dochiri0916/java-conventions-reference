package com.dochiri.tooltestlab.cart.domain.model;

import com.dochiri.tooltestlab.member.domain.model.MemberId;

import static java.util.Objects.requireNonNull;

/**
 * 장바구니 Aggregate Root.
 * 한 회원당 하나의 장바구니만 존재한다.
 */
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

    /**
     * 상품 추가. 이미 담긴 상품이면 수량을 증가시킨다.
     */
    public Cart addItem(String productPublicId, String productName, int quantity, Money unitPrice) {
        requireNonNull(productPublicId, "상품 ID는 null일 수 없습니다.");
        requireNonNull(productName, "상품명은 null일 수 없습니다.");
        requireNonNull(unitPrice, "단가는 null일 수 없습니다.");

        return items.findByProductId(productPublicId)
                .map(existing -> {
                    CartItem increased = existing.increaseQuantity(quantity);
                    return new Cart(id, memberId, items.replace(existing, increased));
                })
                .orElseGet(() -> {
                    CartItem newItem = new CartItem(productPublicId, productName, quantity, unitPrice);
                    return new Cart(id, memberId, items.add(newItem));
                });
    }

    /**
     * 상품 제거.
     */
    public Cart removeItem(String productPublicId) {
        return new Cart(id, memberId, items.remove(productPublicId));
    }

    /**
     * 장바구니 비우기.
     */
    public Cart clear() {
        return new Cart(id, memberId, CartItems.empty());
    }

    public Money totalAmount() {
        return items.totalAmount();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
