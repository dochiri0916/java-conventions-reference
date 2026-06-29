package com.dochiri.tooltestlab.review.application.exception;

public enum ReviewApplicationErrorCode {

    DUPLICATE_REVIEW("중복 리뷰"),
    REVIEW_NOT_FOUND("리뷰 없음"),
    NOT_REVIEW_OWNER("권한 없음");

    private final String title;

    ReviewApplicationErrorCode(final String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}
