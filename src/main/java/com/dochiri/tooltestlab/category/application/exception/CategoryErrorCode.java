package com.dochiri.tooltestlab.category.application.exception;

public enum CategoryErrorCode {

    INVALID_INPUT("입력값이 올바르지 않습니다.");

    private final String message;

    CategoryErrorCode(final String title) {
        this.message = title;
    }

    public String title() {
        return message;
    }
}
