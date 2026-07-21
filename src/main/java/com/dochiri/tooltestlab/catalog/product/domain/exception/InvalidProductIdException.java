package com.dochiri.tooltestlab.catalog.product.domain.exception;

public final class InvalidProductIdException extends RuntimeException {

    private final String productId;

    public InvalidProductIdException(final String productId) {
        super("유효하지 않은 상품 ID입니다.");
        this.productId = productId;
    }

    public String productId() {
        return productId;
    }

}
