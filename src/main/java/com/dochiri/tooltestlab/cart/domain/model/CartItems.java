package com.dochiri.tooltestlab.cart.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * 장바구니 상품 목록에 대한 일급 컬렉션.
 * 0~{@value #MAX_ITEM_COUNT}개의 {@link CartItem}을 보장하며 불변이다.
 * 장바구니는 비어 있을 수 있다(OrderLines와 다른 점).
 */
public record CartItems(List<CartItem> values) {

    public static final int MAX_ITEM_COUNT = 100;

    public CartItems {
        requireNonNull(values, "장바구니 상품 목록은 null일 수 없습니다.");

        if (values.size() > MAX_ITEM_COUNT) {
            throw new IllegalArgumentException(
                    "장바구니 상품은 최대 " + MAX_ITEM_COUNT + "개까지 가능합니다. 입력값: " + values.size());
        }

        // 각 요소 null 검증
        values.forEach(item -> requireNonNull(item, "장바구니 상품은 null일 수 없습니다."));

        values = List.copyOf(values);
    }

    // -------------------------------------------------------------------------
    // Factory
    // -------------------------------------------------------------------------

    public static CartItems empty() {
        return new CartItems(List.of());
    }

    public static CartItems from(List<CartItem> items) {
        return new CartItems(items);
    }

    // -------------------------------------------------------------------------
    // Business
    // -------------------------------------------------------------------------

    /**
     * 전체 금액. 각 상품의 {@link CartItem#totalPrice()} 합계.
     */
    public Money totalAmount() {
        return values.stream()
                .map(CartItem::totalPrice)
                .reduce(Money.ZERO, Money::add);
    }

    /**
     * 장바구니 상품 개수 (라인 수).
     */
    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    /**
     * 특정 상품이 포함되어 있는지 확인한다.
     */
    public boolean containsProduct(String productPublicId) {
        requireNonNull(productPublicId, "상품 ID는 null일 수 없습니다.");
        return values.stream().anyMatch(item -> item.productPublicId().equals(productPublicId));
    }

    /**
     * 상품 ID로 장바구니 아이템을 찾는다.
     */
    public Optional<CartItem> findByProductId(String productPublicId) {
        requireNonNull(productPublicId, "상품 ID는 null일 수 없습니다.");
        return values.stream()
                .filter(item -> item.productPublicId().equals(productPublicId))
                .findFirst();
    }

    // -------------------------------------------------------------------------
    // Collection operations (return new CartItems)
    // -------------------------------------------------------------------------

    /**
     * 상품을 장바구니에 추가한다.
     */
    public CartItems add(CartItem item) {
        requireNonNull(item, "추가할 상품은 null일 수 없습니다.");
        List<CartItem> changed = new ArrayList<>(values);
        changed.add(item);
        return new CartItems(changed);
    }

    /**
     * 기존 상품을 새 상품으로 교체한다(수량 증가 등).
     */
    public CartItems replace(CartItem oldItem, CartItem newItem) {
        requireNonNull(oldItem, "교체 대상 상품은 null일 수 없습니다.");
        requireNonNull(newItem, "새 상품은 null일 수 없습니다.");
        List<CartItem> changed = new ArrayList<>(values);
        int idx = changed.indexOf(oldItem);
        if (idx < 0) {
            throw new IllegalArgumentException("교체 대상 상품이 장바구니에 존재하지 않습니다.");
        }
        changed.set(idx, newItem);
        return new CartItems(changed);
    }

    /**
     * 상품을 장바구니에서 제거한다.
     * 존재하지 않는 상품이면 예외가 발생한다.
     */
    public CartItems remove(String productPublicId) {
        requireNonNull(productPublicId, "상품 ID는 null일 수 없습니다.");

        boolean exists = values.stream()
                .anyMatch(item -> item.productPublicId().equals(productPublicId));
        if (!exists) {
            throw new IllegalArgumentException(
                    "해당 상품이 장바구니에 존재하지 않습니다. productPublicId=" + productPublicId);
        }

        List<CartItem> changed = values.stream()
                .filter(item -> !item.productPublicId().equals(productPublicId))
                .toList();
        return new CartItems(changed);
    }
}
