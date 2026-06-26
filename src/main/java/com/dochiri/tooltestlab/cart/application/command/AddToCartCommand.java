package com.dochiri.tooltestlab.cart.application.command;

public record AddToCartCommand(String memberPublicId, String productPublicId, String productName, int quantity, long unitPrice) {
    public AddToCartCommand {
        if (memberPublicId == null || memberPublicId.isBlank()) throw new IllegalArgumentException("회원 ID는 빈 값일 수 없습니다.");
        if (productPublicId == null || productPublicId.isBlank()) throw new IllegalArgumentException("상품 ID는 빈 값일 수 없습니다.");
        if (quantity <= 0) throw new IllegalArgumentException("수량은 1 이상이어야 합니다. 입력값: " + quantity);
        if (unitPrice < 0) throw new IllegalArgumentException("단가는 0 이상이어야 합니다. 입력값: " + unitPrice);
    }
}
