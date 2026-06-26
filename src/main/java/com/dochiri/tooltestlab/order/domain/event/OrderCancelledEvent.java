package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.order.domain.model.OrderId;

import java.util.List;

/**
 * 주문이 취소되면 발행되는 도메인 이벤트.
 * 재고 복구를 트리거한다.
 */
public record OrderCancelledEvent(
        OrderId orderId,
        List<ItemDetail> items) {

    public record ItemDetail(String productPublicId, int quantity) {
    }
}
