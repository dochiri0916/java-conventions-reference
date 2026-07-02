package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException;
import com.dochiri.tooltestlab.product.domain.exception.InvalidProductIdException;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.product.domain.model.MaxCount;
import com.dochiri.tooltestlab.product.domain.model.Money;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public record OrderLines(List<OrderLine> values) {

    public static final MaxCount MAX_LINE_COUNT = MaxCount.of(100);

    public OrderLines {
        if (values == null) {
            throw InvalidOrderLinesException.required();
        }

        if (values.isEmpty()) {
            throw InvalidOrderLinesException.badOrderLines("주문 상품은 최소 1개 이상이어야 합니다.");
        }
        if (values.size() > MAX_LINE_COUNT.value()) {
            throw InvalidOrderLinesException.badOrderLines(
                    "주문 상품은 최대 " + MAX_LINE_COUNT.value() + "개까지 가능합니다. 입력값: " + values.size());
        }

        if (values.stream().anyMatch(Objects::isNull)) {
            throw InvalidOrderLinesException.badOrderLines("주문 상품은 null일 수 없습니다.");
        }

        values = List.copyOf(values);
    }

    public static OrderLines fromArray(final OrderLine... lines) {
        if (lines == null) {
            throw InvalidOrderLinesException.required();
        }
        return new OrderLines(Arrays.asList(lines));
    }

    public static OrderLines from(final List<OrderLine> lines) {
        return new OrderLines(lines);
    }

    public Money totalAmount() {
        return values.stream()
                .map(OrderLine::totalPrice)
                .reduce(Money.ZERO, Money::add);
    }

    public int size() {
        return values.size();
    }

    public boolean containsProduct(final ProductId productId) {
        if (productId == null) {
            throw InvalidProductIdException.required();
        }
        return values.stream().anyMatch(line -> line.productId().equals(productId));
    }
}
