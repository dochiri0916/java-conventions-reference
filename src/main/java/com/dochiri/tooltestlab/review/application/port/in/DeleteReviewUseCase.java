package com.dochiri.tooltestlab.review.application.port.in;

/**
 * 리뷰 삭제 UseCase. 작성자 본인만 삭제 가능.
 */
public interface DeleteReviewUseCase {
    void delete(String reviewPublicId, String memberPublicId);
}
