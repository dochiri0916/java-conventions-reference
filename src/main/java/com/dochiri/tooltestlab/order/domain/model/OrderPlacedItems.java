package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.event.OrderPlacedEvent;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException;

import java.util.List;

import static java.util.Objects.requireNonNull;

public record OrderPlacedItems(List<OrderPlacedEvent.ItemDetail> values) {

    public OrderPlacedItems {
        requireNonNull(values, "주문 상품 목록은 null일 수 없습니다.");
        if (values.stream().anyMatch(v -> v == null)) {
            throw InvalidOrderLinesException.badOrderLines("주문 상품은 null일 수 없습니다.");
        }
        values = List.copyOf(values);
    }
}
