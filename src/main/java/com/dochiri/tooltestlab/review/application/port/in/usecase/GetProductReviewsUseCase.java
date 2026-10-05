package com.dochiri.tooltestlab.review.application.port.in.usecase;

import com.dochiri.tooltestlab.review.application.port.in.query.GetProductReviewsQuery;
import com.dochiri.tooltestlab.review.application.port.in.result.GetProductReviewsResult;

/**
 * 상품별 리뷰 조회 UseCase.
 */
@FunctionalInterface
public interface GetProductReviewsUseCase {
    GetProductReviewsResult getByProduct(GetProductReviewsQuery query);
}
