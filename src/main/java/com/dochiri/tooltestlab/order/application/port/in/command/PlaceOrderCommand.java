package com.dochiri.tooltestlab.order.application.port.in.command;

public record PlaceOrderCommand(
        String memberPublicId,
        String recipientName,
        String phone,
        String address,
        String zipCode) {
}
