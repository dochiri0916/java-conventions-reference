package com.dochiri.tooltestlab.cart.application.port.in.command;

import com.dochiri.tooltestlab.cart.application.exception.InvalidCommandException;

public record AddToCartCommand(String memberPublicId, String productPublicId, int quantity) {

    public AddToCartCommand {
        if (memberPublicId == null || memberPublicId.isBlank()) {
            throw new InvalidCommandException("회원 ID는 빈 값일 수 없습니다.");
        }
        if (productPublicId == null || productPublicId.isBlank()) {
            throw new InvalidCommandException("상품 ID는 빈 값일 수 없습니다.");
        }
        if (quantity <= 0) {
            throw new InvalidCommandException("수량은 1 이상이어야 합니다. 입력값: " + quantity);
        }
    }
}
