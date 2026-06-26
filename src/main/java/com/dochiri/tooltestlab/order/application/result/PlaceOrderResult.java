package com.dochiri.tooltestlab.order.application.result;

import java.util.List;

public record PlaceOrderResult(String publicId, String status, long totalAmount, List<OrderItemResult> items) {
    public record OrderItemResult(String productPublicId, String productName, int quantity, long unitPrice) {
    }
}
