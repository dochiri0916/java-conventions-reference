package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.review.domain.model.Rating;
import com.dochiri.tooltestlab.review.domain.model.Review;
import com.dochiri.tooltestlab.review.domain.model.ReviewContent;
import com.dochiri.tooltestlab.review.domain.model.ReviewId;

final class ReviewMapper {

    private ReviewMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    static Review toDomain(final ReviewEntity entity) {
        return new Review(
                new ReviewId(entity.getReviewId()),
                new MemberId(entity.getMemberId()),
                new ProductId(entity.getProductId()),
                new Rating(entity.getRating()),
                new ReviewContent(entity.getContent()));
    }

    static ReviewEntity toEntity(final Review domain) {
        return ReviewEntity.create(
                domain.id().value(),
                domain.memberId().value(),
                domain.productId().value(),
                domain.rating().value(),
                domain.content().value());
    }
}
