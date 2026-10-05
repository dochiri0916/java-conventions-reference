package com.dochiri.tooltestlab.order.application.port.in.result;

import java.util.List;

public record GetOrderResult(
        String publicId,
        String memberPublicId,
        String status,
        long totalAmount,
        String recipientName,
        String phone,
        String address,
        String zipCode,
        List<Item> items) {
    public GetOrderResult {
        items = List.copyOf(items);
    }

    public record Item(String productPublicId, String productName, int quantity, long unitPrice, long totalPrice) {
    }
}
