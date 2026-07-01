package com.dochiri.tooltestlab.review.application.exception;

public enum ReviewApplicationErrorCode {

    DUPLICATE_REVIEW("중복 리뷰"),
    REVIEW_NOT_FOUND("리뷰 없음"),
    NOT_REVIEW_OWNER("권한 없음");

    private final String message;

    ReviewApplicationErrorCode(final String title) {
        this.message = title;
    }

    public String title() {
        return message;
    }
}
