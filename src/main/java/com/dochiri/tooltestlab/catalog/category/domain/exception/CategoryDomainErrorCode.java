package com.dochiri.tooltestlab.catalog.category.domain.exception;

import com.dochiri.tooltestlab.global.exception.ErrorCode;
import com.dochiri.tooltestlab.global.exception.ErrorKind;

public enum CategoryDomainErrorCode implements ErrorCode {

    // CATEGORY ID
    CATEGORY_ID_REQUIRED("CATEGORY_ID_REQUIRED", ErrorKind.INVALID_INPUT, "카테고리 ID는 필수입니다."),
    CATEGORY_ID_INVALID("CATEGORY_ID_INVALID", ErrorKind.INVALID_INPUT, "유효하지 않은 카테고리 ID입니다."),

    // CATEGORY NAME
    CATEGORY_NAME_REQUIRED("CATEGORY_NAME_REQUIRED", ErrorKind.INVALID_INPUT, "카테고리명은 필수입니다."),
    CATEGORY_NAME_INVALID("CATEGORY_NAME_INVALID", ErrorKind.INVALID_INPUT, "유효하지 않은 카테고리 이름입니다.");

    private final String code;
    private final ErrorKind kind;
    private final String detail;

    CategoryDomainErrorCode(
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
