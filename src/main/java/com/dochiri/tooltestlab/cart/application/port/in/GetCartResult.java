package com.dochiri.tooltestlab.cart.application.port.in;

import java.util.List;

public record GetCartResult(String publicId, String memberPublicId, List<Item> items, long totalAmount) {
    public GetCartResult {
        items = List.copyOf(items);
    }

    public record Item(String productPublicId, String productName, int quantity, long unitPrice, long totalPrice) {
    }
}
