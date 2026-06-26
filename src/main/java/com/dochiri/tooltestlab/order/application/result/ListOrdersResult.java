package com.dochiri.tooltestlab.order.application.result;

import java.util.List;

public record ListOrdersResult(List<OrderSummary> orders) {
    public record OrderSummary(String publicId, String status, long totalAmount, int itemCount) {
    }
}
