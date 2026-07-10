package com.dochiri.tooltestlab.review.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ApiErrorMessage;
import com.dochiri.tooltestlab.global.error.ApiErrorMessageProvider;
import com.dochiri.tooltestlab.review.application.exception.ReviewApplicationErrorCode;
import com.dochiri.tooltestlab.review.domain.exception.ReviewDomainErrorCode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReviewErrorMessageProvider implements ApiErrorMessageProvider {

    private static final Map<String, ApiErrorMessage> ERROR_MESSAGES = Map.ofEntries(
            message(ReviewDomainErrorCode.INVALID_REVIEW_ID, "리뷰 ID 오류", "리뷰 ID는 공백일 수 없습니다."),
            message(ReviewDomainErrorCode.REVIEW_ID_REQUIRED, "리뷰 ID 누락", "리뷰 ID는 필수입니다."),
            message(ReviewDomainErrorCode.INVALID_RATING, "평점 오류", "평점은 1~5 사이여야 합니다."),
            message(ReviewDomainErrorCode.RATING_REQUIRED, "평점 누락", "평점은 필수입니다."),
            message(ReviewDomainErrorCode.BAD_RVW_CONTENT, "리뷰 내용 오류", "리뷰 내용은 공백일 수 없습니다."),
            message(ReviewDomainErrorCode.REVIEW_CONTENT_REQUIRED, "리뷰 내용 누락", "리뷰 내용은 필수입니다."),
            message(ReviewDomainErrorCode.INVALID_REVIEW, "리뷰 오류", "리뷰 정보가 올바르지 않습니다."),
            message(ReviewDomainErrorCode.REVIEW_REQUIRED, "리뷰 누락", "리뷰 정보는 필수입니다."),
            message(ReviewApplicationErrorCode.DUPLICATE_REVIEW, "중복 리뷰", "이미 해당 상품에 리뷰를 작성했습니다."),
            message(ReviewApplicationErrorCode.REVIEW_NOT_FOUND, "리뷰 없음", "리뷰를 찾을 수 없습니다."),
            message(ReviewApplicationErrorCode.NOT_REVIEW_OWNER, "리뷰 권한 없음", "리뷰를 삭제할 권한이 없습니다."),
            message(ReviewApplicationErrorCode.INVALID_INPUT, "입력값 오류", "입력값이 올바르지 않습니다.")
    );

    @Override
    public Map<String, ApiErrorMessage> errorMessages() {
        return Map.copyOf(ERROR_MESSAGES);
    }

    private static Map.Entry<String, ApiErrorMessage> message(
            final Enum<?> errorCode,
            final String title,
            final String detail) {
        return Map.entry(ApiErrorCode.from(errorCode), new ApiErrorMessage(title, detail));
    }
}
