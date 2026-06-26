package com.dochiri.tooltestlab.review.application.port.in;

import com.dochiri.tooltestlab.review.application.result.GetProductReviewsResult;

/**
 * 상품별 리뷰 조회 UseCase.
 */
public interface GetProductReviewsUseCase {
    GetProductReviewsResult getByProduct(String productPublicId);
}
