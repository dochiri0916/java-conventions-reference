package com.dochiri.tooltestlab.review.domain.exception;

public enum ReviewDomainErrorCode {

    INVALID_REVIEW_ID("리뷰 ID는 공백일 수 없습니다."),
    REVIEW_ID_REQUIRED("리뷰 ID는 null일 수 없습니다."),
    INVALID_RATING("평점은 1~5 사이여야 합니다."),
    RATING_REQUIRED("평점은 null일 수 없습니다."),
    BAD_RVW_CONTENT("리뷰 내용은 빈 값일 수 없습니다."),
    REVIEW_CONTENT_REQUIRED("리뷰 내용은 null일 수 없습니다."),
    INVALID_REVIEW("리뷰 정보가 올바르지 않습니다."),
    REVIEW_REQUIRED("리뷰 정보는 null일 수 없습니다.");

    private final String message;

    ReviewDomainErrorCode(final String message) {
        this.message = message;
    }

    public String title() {
        return message;
    }
}
