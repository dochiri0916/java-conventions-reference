package com.dochiri.tooltestlab.review.application.exception;

public final class ReviewNotFoundException extends ReviewApplicationException {

    public ReviewNotFoundException(final String reviewPublicId) {
        super(ReviewApplicationErrorCode.REVIEW_NOT_FOUND,
                "리뷰를 찾을 수 없습니다. publicId=" + reviewPublicId);
    }
}
