package com.dochiri.tooltestlab.catalog.product.application.exception;

import com.dochiri.tooltestlab.global.exception.ErrorCode;
import com.dochiri.tooltestlab.global.exception.ErrorKind;

public enum ProductApplicationErrorCode implements ErrorCode {

    PRODUCT_NOT_FOUND(
            "PRODUCT_NOT_FOUND",
            ErrorKind.NOT_FOUND,
            "상품을 찾을 수 없습니다."
    ),

    DUPLICATE_PRODUCT_NAME(
            "DUPLICATE_PRODUCT_NAME",
            ErrorKind.CONFLICT,
            "이미 사용 중인 상품명입니다."
    ),

    CATEGORY_NOT_FOUND(
            "CATEGORY_NOT_FOUND",
            ErrorKind.NOT_FOUND,
            "카테고리를 찾을 수 없습니다."
    )


    ;

    private final String code;
    private final ErrorKind kind;
    private final String detail;

    ProductApplicationErrorCode(
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
