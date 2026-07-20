package com.dochiri.tooltestlab.cart.domain.exception;

import com.dochiri.tooltestlab.global.exception.ErrorCode;
import com.dochiri.tooltestlab.global.exception.ErrorKind;

public enum CartDomainErrorCode implements ErrorCode {

    INVALID_CART_ID("CART_ID_INVALID", "유효하지 않은 장바구니 ID입니다."),
    CART_ID_REQUIRED("CART_ID_REQUIRED", "장바구니 ID는 필수입니다."),
    INVALID_CART_ITEM("CART_ITEM_INVALID", "장바구니 상품이 올바르지 않습니다."),
    CART_ITEM_REQUIRED("CART_ITEM_REQUIRED", "장바구니 상품은 필수입니다."),
    INVALID_CART("CART_INVALID", "장바구니 정보가 올바르지 않습니다."),
    CART_REQUIRED("CART_REQUIRED", "장바구니 정보는 필수입니다."),
    TOO_MANY_ITEMS("TOO_MANY_ITEMS", "장바구니 상품은 최대 100개까지 담을 수 있습니다."),
    PRODUCT_ID_REQUIRED("PRODUCT_ID_REQUIRED", "상품 ID는 필수입니다."),
    PRODUCT_NAME_REQUIRED("PRODUCT_NAME_REQUIRED", "상품명은 필수입니다."),
    QUANTITY_REQUIRED("QUANTITY_REQUIRED", "수량은 필수입니다."),
    UNIT_PRICE_REQUIRED("UNIT_PRICE_REQUIRED", "단가는 필수입니다.");

    private final String code;
    private final String detail;

    CartDomainErrorCode(final String code, final String detail) {
        this.code = code;
        this.detail = detail;
    }

    @Override public String code() { return code; }
    @Override public ErrorKind kind() { return ErrorKind.INVALID_INPUT; }
    @Override public String detail() { return detail; }
}
