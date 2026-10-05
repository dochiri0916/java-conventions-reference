package com.dochiri.tooltestlab.order.application.port.in.result;

import java.util.List;

public record PlaceOrderResult(String publicId, String status, long totalAmount, List<OrderItemResult> items) {
    public PlaceOrderResult {
        items = List.copyOf(items);
    }

    public record OrderItemResult(String productPublicId, String productName, int quantity, long unitPrice) {
    }
}
