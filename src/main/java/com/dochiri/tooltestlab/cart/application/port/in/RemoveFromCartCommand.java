package com.dochiri.tooltestlab.cart.application.port.in;

import com.dochiri.tooltestlab.cart.application.exception.InvalidCommandException;

public record RemoveFromCartCommand(String memberPublicId, String productPublicId) {
    public RemoveFromCartCommand {
        if (memberPublicId == null || memberPublicId.isBlank()) {
            throw InvalidCommandException.invalidInput("회원 ID는 빈 값일 수 없습니다.");
        }
        if (productPublicId == null || productPublicId.isBlank()) {
            throw InvalidCommandException.invalidInput("상품 ID는 빈 값일 수 없습니다.");
        }
    }
}
