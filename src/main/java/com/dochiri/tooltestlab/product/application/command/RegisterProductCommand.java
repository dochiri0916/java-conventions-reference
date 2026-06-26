package com.dochiri.tooltestlab.product.application.command;

import java.util.List;

public record RegisterProductCommand(String name, String description, long priceAmount, int stock, List<String> categoryIds) {
    public RegisterProductCommand {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("상품명은 빈 값일 수 없습니다.");
        if (priceAmount < 0) throw new IllegalArgumentException("가격은 0 이상이어야 합니다. 입력값: " + priceAmount);
        if (stock < 0) throw new IllegalArgumentException("재고는 0 이상이어야 합니다. 입력값: " + stock);
        if (categoryIds == null) categoryIds = List.of();
    }
}
