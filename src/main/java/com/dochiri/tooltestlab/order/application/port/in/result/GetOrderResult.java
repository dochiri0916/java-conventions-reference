package com.dochiri.tooltestlab.order.application.port.in.result;

public record GetOrderResult(
        String publicId,
        String memberPublicId,
        String status,
        long totalAmount,
        String recipientName,
        String phone,
        String address,
        String zipCode,
        GetOrderResultItems items) {

    public record Item(String productPublicId, String productName, int quantity, long unitPrice, long totalPrice) {
    }
}
