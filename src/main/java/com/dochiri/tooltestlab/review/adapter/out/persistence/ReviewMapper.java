package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import com.dochiri.tooltestlab.review.domain.model.Review;
import com.dochiri.tooltestlab.review.domain.model.ReviewId;

final class ReviewMapper {

    private ReviewMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    static Review toDomain(ReviewEntity entity) {
        return new Review(
                new ReviewId(entity.getPublicId()),
                new MemberId(entity.getMemberPublicId()),
                new ProductId(entity.getProductPublicId()),
                entity.getRating(),
                entity.getContent());
    }

    static ReviewEntity toEntity(Review domain) {
        return new ReviewEntity(
                null,
                domain.id().value(),
                domain.memberId().value(),
                domain.productId().value(),
                domain.rating(),
                domain.content());
    }
}
