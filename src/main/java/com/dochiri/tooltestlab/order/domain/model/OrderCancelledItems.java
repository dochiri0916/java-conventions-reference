package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.event.OrderCancelled;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException;

import java.util.List;

import static java.util.Objects.requireNonNull;

public record OrderCancelledItems(List<OrderCancelled.ItemDetail> values) {

    public OrderCancelledItems {
        requireNonNull(values, "주문 상품 목록은 null일 수 없습니다.");
        if (values.stream().anyMatch(v -> v == null)) {
            throw InvalidOrderLinesException.badOrderLines("주문 상품은 null일 수 없습니다.");
        }
        values = List.copyOf(values);
    }
}
