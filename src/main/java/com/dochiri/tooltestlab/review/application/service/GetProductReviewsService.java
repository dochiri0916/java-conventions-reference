package com.dochiri.tooltestlab.review.application.service;

import com.dochiri.tooltestlab.review.application.port.in.query.GetProductReviewsQuery;
import com.dochiri.tooltestlab.review.application.port.in.result.GetProductReviewsResult;
import com.dochiri.tooltestlab.review.application.port.in.usecase.GetProductReviewsUseCase;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.application.ReviewResultMapper;
import com.dochiri.tooltestlab.review.application.port.out.Reviews;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class GetProductReviewsService implements GetProductReviewsUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetProductReviewsResult getByProduct(final GetProductReviewsQuery query) {
        final ProductReferenceId productId = ProductReferenceId.parse(
                query.productPublicId());
        final Reviews reviews = reviewRepositoryPort.findByProductId(productId);
        return ReviewResultMapper.toProductReviewsResult(productId, reviews);
    }
}
