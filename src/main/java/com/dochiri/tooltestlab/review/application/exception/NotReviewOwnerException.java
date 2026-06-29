package com.dochiri.tooltestlab.review.application.exception;

public final class NotReviewOwnerException extends ReviewApplicationException {

    public NotReviewOwnerException() {
        super(ReviewApplicationErrorCode.NOT_REVIEW_OWNER, "본인이 작성한 리뷰만 삭제할 수 있습니다.");
    }
}
