package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;
import com.dochiri.tooltestlab.order.domain.exception.InvalidOrderLinesException;

import java.util.List;

public record OrderPlacedItems(List<OrderPlaced.ItemDetail> values) {

    public OrderPlacedItems {
        if (values == null) {
            throw InvalidOrderLinesException.required();
        }
        if (values.stream().anyMatch(v -> v == null)) {
            throw InvalidOrderLinesException.badOrderLines("주문 상품은 null일 수 없습니다.");
        }
        values = List.copyOf(values);
    }
}
