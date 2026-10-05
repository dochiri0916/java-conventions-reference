package com.dochiri.tooltestlab.order.application.port.in.result;

import java.util.List;

public record ListOrdersResult(List<OrderSummary> orders) {
    public ListOrdersResult {
        orders = List.copyOf(orders);
    }

    public record OrderSummary(String publicId, String status, long totalAmount, int itemCount) {
    }
}
