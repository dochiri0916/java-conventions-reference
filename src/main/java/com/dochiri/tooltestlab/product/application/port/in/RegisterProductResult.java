package com.dochiri.tooltestlab.product.application.port.in;

import java.util.List;

public record RegisterProductResult(
        String publicId, String name, long priceAmount, int stock, String status,
        List<String> categoryNames) {
}
