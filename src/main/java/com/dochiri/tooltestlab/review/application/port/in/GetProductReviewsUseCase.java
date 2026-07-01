package com.dochiri.tooltestlab.review.application.port.in;

/**
 * 상품별 리뷰 조회 UseCase.
 */
@FunctionalInterface
public interface GetProductReviewsUseCase {
    GetProductReviewsResult getByProduct(String productPublicId);
}
