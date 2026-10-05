package com.dochiri.tooltestlab.order.application.port.in.command;

public record UpdateCartSnapshotCommand(
        String cartId, String memberId, long revision, long sequence, CartSnapshotItems items) {

    public record Item(String productId, String productName, int quantity, long unitPrice) {
    }
}
