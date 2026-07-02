package com.dochiri.tooltestlab.product.domain.exception;

public enum ProductDomainErrorCode {

    LOW_STOCK("재고가 부족합니다."),
    BAD_PRODUCT_ID("상품 ID는 빈 값일 수 없습니다."),
    PRODUCT_ID_REQUIRED("상품 ID는 null일 수 없습니다."),
    BAD_PRODUCT_NAME("상품명은 빈 값일 수 없습니다."),
    PRODUCT_NAME_REQUIRED("상품명은 null일 수 없습니다."),
    INVALID_STOCK("재고 값이 올바르지 않습니다."),
    STOCK_REQUIRED("재고는 null일 수 없습니다."),
    INVALID_PRODUCT("상품 정보가 올바르지 않습니다."),
    PRODUCT_REQUIRED("상품 정보는 null일 수 없습니다."),
    MONEY_NEGATIVE("금액은 0 이상이어야 합니다."),
    MONEY_ADD_NULL("더할 금액은 null일 수 없습니다."),
    MONEY_REQUIRED("금액은 null일 수 없습니다."),
    MONEY_MULTIPLY_NEGATIVE("수량은 0 이상이어야 합니다."),
    MAX_COUNT_NOT_POSITIVE("최대 개수는 0보다 커야 합니다.");

    private final String message;

    ProductDomainErrorCode(final String message) {
        this.message = message;
    }

    public String title() {
        return message;
    }
}
