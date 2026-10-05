package com.dochiri.tooltestlab.cart.application.port.in.command;

public record UpdateProductSnapshotCommand(String productId, String name, long priceAmount, long sequence) {
}
