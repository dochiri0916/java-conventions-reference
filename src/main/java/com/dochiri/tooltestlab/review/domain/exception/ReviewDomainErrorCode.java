package com.dochiri.tooltestlab.review.domain.exception;

import com.dochiri.tooltestlab.global.exception.ErrorCode;
import com.dochiri.tooltestlab.global.exception.ErrorKind;

public enum ReviewDomainErrorCode implements ErrorCode {

    INVALID_REVIEW_ID("REVIEW_ID_INVALID", "유효하지 않은 리뷰 ID입니다."),
    REVIEW_ID_REQUIRED("REVIEW_ID_REQUIRED", "리뷰 ID는 필수입니다."),
    INVALID_RATING("RATING_INVALID", "평점은 1에서 5 사이여야 합니다."),
    RATING_REQUIRED("RATING_REQUIRED", "평점은 필수입니다."),
    BAD_RVW_CONTENT("REVIEW_CONTENT_INVALID", "리뷰 내용이 올바르지 않습니다."),
    REVIEW_CONTENT_REQUIRED("REVIEW_CONTENT_REQUIRED", "리뷰 내용은 필수입니다."),
    INVALID_REVIEW("REVIEW_INVALID", "리뷰 정보가 올바르지 않습니다."),
    REVIEW_REQUIRED("REVIEW_REQUIRED", "리뷰 정보는 필수입니다.");

    private final String code;
    private final String detail;

    ReviewDomainErrorCode(final String code, final String detail) {
        this.code = code;
        this.detail = detail;
    }

    @Override public String code() { return code; }
    @Override public ErrorKind kind() { return ErrorKind.INVALID_INPUT; }
    @Override public String detail() { return detail; }
}
