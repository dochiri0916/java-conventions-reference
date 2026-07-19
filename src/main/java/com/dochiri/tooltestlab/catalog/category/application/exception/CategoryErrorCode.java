package com.dochiri.tooltestlab.catalog.category.application.exception;

import com.dochiri.tooltestlab.global.exception.ErrorCode;
import com.dochiri.tooltestlab.global.exception.ErrorKind;

public enum CategoryErrorCode implements ErrorCode {

    INVALID_INPUT("CATEGORY_INVALID_INPUT", ErrorKind.INVALID_INPUT, "카테고리 입력값이 올바르지 않습니다."),
    DUPLICATE_CATEGORY_NAME("DUPLICATE_CATEGORY_NAME", ErrorKind.CONFLICT, "이미 등록된 카테고리명입니다.");

    private final String code;
    private final ErrorKind kind;
    private final String detail;

    CategoryErrorCode(final String code, final ErrorKind kind, final String detail) {
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
