package com.dochiri.tooltestlab.order.domain.model;

import com.dochiri.tooltestlab.order.domain.event.OrderPlaced;
import com.dochiri.tooltestlab.order.domain.exception.OrderDomainException;

import java.util.List;
import java.util.Objects;

public record OrderPlacedItems(List<OrderPlaced.ItemDetail> values) {

    public OrderPlacedItems {
        if (values == null) {
            throw OrderDomainException.orderLinesRequired();
        }
        if (values.stream().anyMatch(Objects::isNull)) {
            throw OrderDomainException.badOrderLines("주문 상품은 null일 수 없습니다.");
        }
        values = List.copyOf(values);
    }
}
