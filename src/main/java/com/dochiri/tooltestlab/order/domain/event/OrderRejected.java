package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.order.domain.vo.OrderId;

public record OrderRejected(OrderId orderId, String reason) {
}
