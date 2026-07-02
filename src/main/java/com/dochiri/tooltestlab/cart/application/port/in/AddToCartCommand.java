package com.dochiri.tooltestlab.cart.application.port.in;

import com.dochiri.tooltestlab.cart.application.exception.InvalidCommandException;

public record AddToCartCommand(String memberPublicId, String productPublicId, int quantity) {

    public AddToCartCommand {
        if (memberPublicId == null || memberPublicId.isBlank()) {
            throw InvalidCommandException.invalidInput("회원 ID는 빈 값일 수 없습니다.");
        }
        if (productPublicId == null || productPublicId.isBlank()) {
            throw InvalidCommandException.invalidInput("상품 ID는 빈 값일 수 없습니다.");
        }
        if (quantity <= 0) {
            throw InvalidCommandException.invalidInput("수량은 1 이상이어야 합니다. 입력값: " + quantity);
        }
    }
}
