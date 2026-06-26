package com.dochiri.tooltestlab.product.application.result;

import java.util.List;

public record RegisterProductResult(String publicId, String name, long priceAmount, int stock, String status, List<String> categoryNames) {
}
