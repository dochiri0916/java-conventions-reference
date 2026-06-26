package com.dochiri.tooltestlab.order.domain.model;

import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * 주문 상품 목록에 대한 일급 컬렉션.
 * 비어있지 않은 1~{@value #MAX_LINE_COUNT}개의 {@link OrderLine}을 보장하며 불변이다.
 */
public record OrderLines(List<OrderLine> values) {

    public static final int MAX_LINE_COUNT = 100;

    public OrderLines {
        requireNonNull(values, "주문 상품 목록은 null일 수 없습니다.");

        if (values.isEmpty()) {
            throw new IllegalArgumentException("주문 상품은 최소 1개 이상이어야 합니다.");
        }
        if (values.size() > MAX_LINE_COUNT) {
            throw new IllegalArgumentException(
                    "주문 상품은 최대 " + MAX_LINE_COUNT + "개까지 가능합니다. 입력값: " + values.size());
        }

        // 각 요소 null 검증
        values.forEach(line -> requireNonNull(line, "주문 상품은 null일 수 없습니다."));

        values = List.copyOf(values);
    }

    // -------------------------------------------------------------------------
    // Factory
    // -------------------------------------------------------------------------

    public static OrderLines of(OrderLine... lines) {
        requireNonNull(lines, "주문 상품 목록은 null일 수 없습니다.");
        return new OrderLines(Arrays.asList(lines));
    }

    public static OrderLines from(List<OrderLine> lines) {
        return new OrderLines(lines);
    }

    // -------------------------------------------------------------------------
    // Business
    // -------------------------------------------------------------------------

    /**
     * 전체 주문 금액. 각 상품의 {@link OrderLine#totalPrice()} 합계.
     */
    public Money totalAmount() {
        return Money.of(values.stream().mapToLong(OrderLine::totalPrice).sum());
    }

    /**
     * 상품 개수 (주문 라인 수).
     */
    public int size() {
        return values.size();
    }

    /**
     * 특정 상품이 포함되어 있는지 확인한다.
     */
    public boolean containsProduct(String productPublicId) {
        requireNonNull(productPublicId, "상품 ID는 null일 수 없습니다.");
        return values.stream().anyMatch(line -> line.productPublicId().equals(productPublicId));
    }
}
