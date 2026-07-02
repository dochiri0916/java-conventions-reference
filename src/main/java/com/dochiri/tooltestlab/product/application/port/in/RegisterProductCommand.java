package com.dochiri.tooltestlab.product.application.port.in;

import com.dochiri.tooltestlab.product.application.exception.InvalidCommandException;

import java.util.List;

public record RegisterProductCommand(
        String name, String description, long priceAmount, int stock, List<String> categoryIds) {
    public RegisterProductCommand {
        if (name == null || name.isBlank()) {
            throw InvalidCommandException.invalidInput("상품명은 빈 값일 수 없습니다.");
        }
        if (priceAmount < 0) {
            throw InvalidCommandException.invalidInput("가격은 0 이상이어야 합니다. 입력값: " + priceAmount);
        }
        if (stock < 0) {
            throw InvalidCommandException.invalidInput("재고는 0 이상이어야 합니다. 입력값: " + stock);
        }
        categoryIds = categoryIds != null ? List.copyOf(categoryIds) : List.of();
    }
}
