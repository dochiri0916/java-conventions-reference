package com.dochiri.tooltestlab.order.application.port.in.result;

public record PlaceOrderResult(
        String publicId,
        String status,
        long totalAmount,
        String recipientName,
        String phone,
        String address,
        String zipCode,
        PlaceOrderResultItems items) {

    public record OrderItemResult(String productPublicId, String productName, int quantity, long unitPrice) {
    }
}
