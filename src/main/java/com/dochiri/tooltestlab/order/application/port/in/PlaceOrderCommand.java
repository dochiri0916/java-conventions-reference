package com.dochiri.tooltestlab.order.application.port.in;

public record PlaceOrderCommand(
        String memberPublicId,
        String recipientName,
        String phone,
        String address,
        String zipCode) {
}
