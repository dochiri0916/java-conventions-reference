package com.dochiri.tooltestlab.order.domain.event;

import com.dochiri.tooltestlab.order.domain.Order;

public record OrderCancelled(Order order) {
}
