package com.dochiri.tooltestlab.cart.application.port.in.result;

public record GetCartResult(String publicId, String memberPublicId, CartResultItems items, long totalAmount) {

    public record Item(String productPublicId, String productName, int quantity, long unitPrice, long totalPrice) {
    }
}
