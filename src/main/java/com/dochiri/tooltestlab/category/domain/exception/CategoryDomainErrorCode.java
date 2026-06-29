package com.dochiri.tooltestlab.category.domain.exception;

public enum CategoryDomainErrorCode {

    INVALID_CAT_ID("카테고리 ID는 공백일 수 없습니다."),
    INVALID_CAT_NAME("카테고리명은 빈 값일 수 없습니다.");

    private final String message;

    CategoryDomainErrorCode(final String message) {
        this.message = message;
    }

    public String title() {
        return message;
    }
}
