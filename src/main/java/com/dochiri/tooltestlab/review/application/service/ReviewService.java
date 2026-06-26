package com.dochiri.tooltestlab.review.application.service;

import com.dochiri.tooltestlab.review.application.command.WriteReviewCommand;
import com.dochiri.tooltestlab.review.application.port.in.DeleteReviewUseCase;
import com.dochiri.tooltestlab.review.application.port.in.GetProductReviewsUseCase;
import com.dochiri.tooltestlab.review.application.port.in.WriteReviewUseCase;
import com.dochiri.tooltestlab.review.application.port.out.ReviewRepositoryPort;
import com.dochiri.tooltestlab.review.application.result.GetProductReviewsResult;
import com.dochiri.tooltestlab.review.application.result.WriteReviewResult;
import com.dochiri.tooltestlab.review.domain.model.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService implements WriteReviewUseCase, GetProductReviewsUseCase, DeleteReviewUseCase {

    private final ReviewRepositoryPort reviewRepositoryPort;

    public ReviewService(ReviewRepositoryPort reviewRepositoryPort) {
        this.reviewRepositoryPort = reviewRepositoryPort;
    }

    @Override
    public WriteReviewResult write(WriteReviewCommand command) {
        if (reviewRepositoryPort.existsByMemberIdAndProductId(command.memberPublicId(), command.productPublicId())) {
            throw new IllegalStateException("이미 이 상품에 리뷰를 작성하셨습니다.");
        }

        Review review = Review.create(command.memberPublicId(), command.productPublicId(), command.rating(), command.content());
        Review saved = reviewRepositoryPort.save(review);

        return new WriteReviewResult(saved.publicId(), saved.memberPublicId(), saved.productPublicId(), saved.rating(), saved.content());
    }

    @Override
    @Transactional(readOnly = true)
    public GetProductReviewsResult getByProduct(String productPublicId) {
        List<Review> reviews = reviewRepositoryPort.findByProductId(productPublicId);

        double avg = reviews.isEmpty() ? 0.0
                : reviews.stream().mapToInt(Review::rating).average().orElse(0.0);

        var data = reviews.stream()
                .map(r -> new GetProductReviewsResult.ReviewData(r.publicId(), r.memberPublicId(), r.rating(), r.content()))
                .toList();

        return new GetProductReviewsResult(productPublicId, data, Math.round(avg * 10.0) / 10.0);
    }

    @Override
    public void delete(String reviewPublicId, String memberPublicId) {
        Review review = reviewRepositoryPort.findByPublicId(reviewPublicId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다. publicId=" + reviewPublicId));

        if (!review.memberPublicId().equals(memberPublicId)) {
            throw new IllegalStateException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        reviewRepositoryPort.deleteByPublicId(reviewPublicId);
    }
}
