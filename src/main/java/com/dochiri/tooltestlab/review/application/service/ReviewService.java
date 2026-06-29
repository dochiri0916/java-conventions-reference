package com.dochiri.tooltestlab.review.application.service;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.review.application.command.WriteReviewCommand;
import com.dochiri.tooltestlab.review.application.exception.DuplicateReviewException;
import com.dochiri.tooltestlab.review.application.exception.NotReviewOwnerException;
import com.dochiri.tooltestlab.review.application.exception.ReviewNotFoundException;
import com.dochiri.tooltestlab.review.application.port.in.DeleteReviewUseCase;
import com.dochiri.tooltestlab.review.application.port.in.GetProductReviewsUseCase;
import com.dochiri.tooltestlab.review.application.port.in.WriteReviewUseCase;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.application.result.GetProductReviewsResult;
import com.dochiri.tooltestlab.review.application.result.WriteReviewResult;
import com.dochiri.tooltestlab.review.domain.model.Rating;
import com.dochiri.tooltestlab.review.domain.model.Review;
import com.dochiri.tooltestlab.review.domain.model.ReviewContent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService implements WriteReviewUseCase, GetProductReviewsUseCase, DeleteReviewUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;

    public ReviewService(final ReviewRepositoryPort reviewRepositoryPort) {
        this.reviewRepositoryPort = reviewRepositoryPort;
    }

    @Override
    public WriteReviewResult write(final WriteReviewCommand command) {
        if (reviewRepositoryPort.existsByMemberIdAndProductId(command.memberPublicId(), command.productPublicId())) {
            throw new DuplicateReviewException();
        }

        final Review review = Review.create(
                new MemberId(command.memberPublicId()),
                new ProductId(command.productPublicId()),
                new Rating(command.rating()),
                new ReviewContent(command.content()));
        final Review saved = reviewRepositoryPort.save(review);

        return new WriteReviewResult(saved.id().value(), saved.memberId().value(),
                saved.productId().value(), saved.rating().value(), saved.content().value());
    }

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

    @Override
    public void delete(final String reviewPublicId, final String memberPublicId) {
        final Review review = reviewRepositoryPort.findByPublicId(reviewPublicId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewPublicId));

        if (!review.memberId().value().equals(memberPublicId)) {
            throw new NotReviewOwnerException();
        }

        reviewRepositoryPort.deleteByPublicId(reviewPublicId);
    }
}
