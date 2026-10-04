package com.dochiri.tooltestlab.order.application.port.in;

import java.util.List;

public record UpdateCartSnapshotCommand(
        String cartId, String memberId, long revision, long sequence, List<Item> items) {
    public UpdateCartSnapshotCommand {
        items = List.copyOf(items);
    }

    public record Item(String productId, String productName, int quantity, long unitPrice) {
    }
}
