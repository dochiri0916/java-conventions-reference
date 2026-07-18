package com.dochiri.tooltestlab.catalog.product.domain.exception;

import com.dochiri.tooltestlab.global.exception.ErrorCode;
import com.dochiri.tooltestlab.global.exception.ErrorKind;

public enum ProductDomainErrorCode implements ErrorCode {

    // ID
    PRODUCT_ID_REQUIRED("PRODUCT_ID_REQUIRED", ErrorKind.INVALID_INPUT, "상품 ID는 필수입니다."),
    PRODUCT_ID_INVALID("PRODUCT_ID_INVALID", ErrorKind.INVALID_INPUT, "유효하지 않은 상품 ID입니다."),

    // NAME
    PRODUCT_NAME_REQUIRED("PRODUCT_NAME_REQUIRED", ErrorKind.INVALID_INPUT, "상품명은 필수입니다."),
    PRODUCT_NAME_INVALID("PRODUCT_NAME_INVALID", ErrorKind.INVALID_INPUT, "유효하지 않은 상품명입니다."),

    // DESCRIPTION
    PRODUCT_DESCRIPTION_REQUIRED("PRODUCT_DESCRIPTION_REQUIRED", ErrorKind.INVALID_INPUT, "상품 설명은 필수입니다."),
    PRODUCT_DESCRIPTION_TOO_SHORT("PRODUCT_DESCRIPTION_TOO_SHORT", ErrorKind.INVALID_INPUT, "상품 설명이 최소 길이보다 짧습니다."),
    PRODUCT_DESCRIPTION_TOO_LONG("PRODUCT_DESCRIPTION_TOO_LONG", ErrorKind.INVALID_INPUT, "상품 설명이 최대 길이보다 깁니다."),

    // MONEY
    MONEY_NEGATIVE("MONEY_NEGATIVE", ErrorKind.INVALID_INPUT, "금액은 0원 이상이어야 합니다."),
    MONEY_MULTIPLY_NEGATIVE("MONEY_MULTIPLY_NEGATIVE", ErrorKind.INVALID_INPUT, "수량은 0개 이상이어야 합니다."),
    MONEY_OVERFLOW("MONEY_OVERFLOW", ErrorKind.INVALID_INPUT, "금액 계산 범위를 초과했습니다."),

    // STOCK
    STOCK_NEGATIVE("STOCK_NEGATIVE", ErrorKind.INVALID_INPUT, "재고는 0 이상이어야 합니다."),
    STOCK_QUANTITY_INVALID("STOCK_QUANTITY_INVALID", ErrorKind.INVALID_INPUT, "재고 변경 수량은 1 이상이어야 합니다."),
    INSUFFICIENT_STOCK("INSUFFICIENT_STOCK", ErrorKind.CONFLICT, "상품 재고가 부족합니다."),
    STOCK_OVERFLOW("STOCK_OVERFLOW", ErrorKind.INVALID_INPUT, "재고가 허용 범위를 초과합니다."),

    // PRODUCT_STATUS
    PRODUCT_STATUS_TRANSITION_NOT_ALLOWED("PRODUCT_STATUS_TRANSITION_NOT_ALLOWED", ErrorKind.INVALID_STATE, "허용되지 않는 상품 상태 변경입니다."),
    INVALID_PRODUCT_STATE("INVALID_PRODUCT_STATE", ErrorKind.INVALID_STATE, "현재 상품 상태에서는 요청한 작업을 수행할 수 없습니다.");

    private final String code;
    private final ErrorKind kind;
    private final String detail;

    ProductDomainErrorCode(
            final String code,
            final ErrorKind kind,
            final String detail
    ) {
        this.code = code;
        this.kind = kind;
        this.detail = detail;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public ErrorKind kind() {
        return kind;
    }

    @Override
    public String detail() {
        return detail;
    }

}
