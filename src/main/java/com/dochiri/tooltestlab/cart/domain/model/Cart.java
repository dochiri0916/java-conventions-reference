package com.dochiri.tooltestlab.cart.domain.model;

import java.util.*;

/**
 * 장바구니 Aggregate Root. 회원당 하나의 장바구니.
 */
public record Cart(String publicId, String memberPublicId, List<CartItem> items) {

    public Cart {
        Objects.requireNonNull(publicId, "publicId는 null일 수 없습니다.");
        Objects.requireNonNull(memberPublicId, "회원 ID는 null일 수 없습니다.");
        Objects.requireNonNull(items, "장바구니 아이템 목록은 null일 수 없습니다.");

        if (publicId.isBlank()) throw new IllegalArgumentException("publicId는 빈 값일 수 없습니다.");
        if (memberPublicId.isBlank()) throw new IllegalArgumentException("회원 ID는 빈 값일 수 없습니다.");

        // 방어적 복사
        items = Collections.unmodifiableList(new ArrayList<>(items));
    }

    public static Cart create(String memberPublicId) {
        return new Cart(
                UUID.randomUUID().toString().replace("-", ""),
                memberPublicId,
                List.of());
    }

    /**
     * 상품 추가. 이미 있으면 수량 증가.
     */
    public Cart addItem(String productPublicId, String productName, int quantity, long unitPrice) {
        List<CartItem> newItems = new ArrayList<>(this.items);
        Optional<CartItem> existing = findItem(productPublicId);

        if (existing.isPresent()) {
            int idx = newItems.indexOf(existing.get());
            newItems.set(idx, existing.get().increaseQuantity(quantity));
        } else {
            newItems.add(new CartItem(productPublicId, productName, quantity, unitPrice));
        }

        return new Cart(publicId, memberPublicId, newItems);
    }

    /**
     * 상품 제거.
     */
    public Cart removeItem(String productPublicId) {
        List<CartItem> newItems = this.items.stream()
                .filter(item -> !item.productPublicId().equals(productPublicId))
                .toList();
        return new Cart(publicId, memberPublicId, newItems);
    }

    /**
     * 장바구니 비우기.
     */
    public Cart clear() {
        return new Cart(publicId, memberPublicId, List.of());
    }

    public long totalAmount() {
        return items.stream().mapToLong(CartItem::totalPrice).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private Optional<CartItem> findItem(String productPublicId) {
        return items.stream().filter(i -> i.productPublicId().equals(productPublicId)).findFirst();
    }
}
