package com.dochiri.tooltestlab.catalog.domain.event;

import com.dochiri.tooltestlab.catalog.domain.vo.OrderReferenceId;

public record StockReleased(OrderReferenceId orderId) {
}
