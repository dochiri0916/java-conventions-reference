package com.dochiri.tooltestlab.catalog.application.port.in;

import java.util.List;

public record ReserveOrderStockCommand(String orderId, List<Item> items) {
    public ReserveOrderStockCommand {
        items = List.copyOf(items);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("재고 예약 항목이 필요합니다.");
        }
    }

    public record Item(String productId, int quantity) {
    }
}
