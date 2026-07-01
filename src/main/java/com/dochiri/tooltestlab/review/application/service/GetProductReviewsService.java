package com.dochiri.tooltestlab.review.application.service;

import com.dochiri.tooltestlab.review.application.port.in.GetProductReviewsResult;
import com.dochiri.tooltestlab.review.application.port.in.GetProductReviewsUseCase;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.domain.model.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class GetProductReviewsService implements GetProductReviewsUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetProductReviewsResult getByProduct(final String productPublicId) {
        final List<Review> reviews = reviewRepositoryPort.findByProductId(productPublicId);

        final double avg = reviews.isEmpty() ? 0.0
                : reviews.stream().mapToInt(r -> r.rating().value()).average().orElse(0.0);

        final var data = reviews.stream()
                .map(r -> new GetProductReviewsResult.ReviewData(
                        r.id().value(), r.memberId().value(), r.rating().value(), r.content().value()))
                .toList();

        return new GetProductReviewsResult(productPublicId, data, Math.round(avg * 10.0) / 10.0);
    }
}
