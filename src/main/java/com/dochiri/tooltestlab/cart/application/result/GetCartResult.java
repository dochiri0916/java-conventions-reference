package com.dochiri.tooltestlab.cart.application.result;

import java.util.List;

public record GetCartResult(String publicId, String memberPublicId, List<Item> items, long totalAmount) {

    public record Item(String productPublicId, String productName, int quantity, long unitPrice, long totalPrice) {
    }
}
