package com.dochiri.tooltestlab.cart.application.port.in;

public record UpdateProductSnapshotCommand(String productId, String name, long priceAmount, long sequence) {
}
