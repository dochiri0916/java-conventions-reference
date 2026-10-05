package com.dochiri.tooltestlab.review.application;

import com.dochiri.tooltestlab.review.application.port.in.result.GetProductReviewsResult;
import com.dochiri.tooltestlab.review.application.port.in.result.ReviewDataItems;
import com.dochiri.tooltestlab.review.application.port.in.result.WriteReviewResult;
import com.dochiri.tooltestlab.review.application.port.out.Reviews;
import com.dochiri.tooltestlab.review.domain.Review;
import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.Rating;
import com.dochiri.tooltestlab.review.domain.vo.ReviewContent;
import com.dochiri.tooltestlab.review.domain.vo.ReviewId;

import java.util.List;

public final class ReviewResultMapper {

    private static final double AVERAGE_SCALE = 10.0;

    private ReviewResultMapper() {
    }

    public static WriteReviewResult toWriteResult(final Review review) {
        final ReviewId id = review.id();
        final MemberReferenceId memberId = review.memberId();
        final ProductReferenceId productId = review.productId();
        final Rating rating = review.rating();
        final ReviewContent content = review.content();
        return new WriteReviewResult(
                id.value(),
                memberId.value(),
                productId.value(),
                rating.value(),
                content.value());
    }

    public static GetProductReviewsResult toProductReviewsResult(
            final ProductReferenceId productId,
            final Reviews found) {
        final List<Review> reviews = found.values();
        final List<GetProductReviewsResult.ReviewData> data = reviews.stream()
                .map(ReviewResultMapper::toReviewData)
                .toList();
        return new GetProductReviewsResult(
                productId.value(),
                new ReviewDataItems(data),
                averageRating(reviews));
    }

    private static GetProductReviewsResult.ReviewData toReviewData(final Review review) {
        final ReviewId id = review.id();
        final MemberReferenceId memberId = review.memberId();
        final Rating rating = review.rating();
        final ReviewContent content = review.content();
        return new GetProductReviewsResult.ReviewData(
                id.value(),
                memberId.value(),
                rating.value(),
                content.value());
    }

    private static double averageRating(final List<Review> reviews) {
        final double average = reviews.stream()
                .mapToInt(ReviewResultMapper::ratingValue)
                .average()
                .orElse(0.0);
        return Math.round(average * AVERAGE_SCALE) / AVERAGE_SCALE;
    }

    private static int ratingValue(final Review review) {
        final Rating rating = review.rating();
        return rating.value();
    }
}
