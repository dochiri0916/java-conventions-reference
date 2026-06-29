package com.dochiri.tooltestlab.product.domain.exception;

public enum ProductDomainErrorCode {

    LOW_STOCK("재고가 부족합니다."),
    BAD_PRODUCT_ID("상품 ID는 빈 값일 수 없습니다."),
    BAD_PRODUCT_NAME("상품명은 빈 값일 수 없습니다."),
    INVALID_STOCK("재고 값이 올바르지 않습니다."),
    INVALID_PRODUCT("상품 정보가 올바르지 않습니다.");

    private final String message;

    ProductDomainErrorCode(final String message) {
        this.message = message;
    }

    public String title() {
        return message;
    }
}
