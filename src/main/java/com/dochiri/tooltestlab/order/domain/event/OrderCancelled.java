package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.order.domain.model.OrderCancelledItems;
import com.dochiri.tooltestlab.order.domain.model.OrderId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.product.domain.model.ProductId;

import static java.util.Objects.requireNonNull;

public record OrderCancelled(
        OrderId orderId,
        OrderCancelledItems items) {

    public OrderCancelled {
        requireNonNull(orderId, "주문 ID는 null일 수 없습니다.");
        requireNonNull(items, "주문 상품 목록은 null일 수 없습니다.");
    }

    public record ItemDetail(ProductId productId, Quantity quantity) {
    }
}
