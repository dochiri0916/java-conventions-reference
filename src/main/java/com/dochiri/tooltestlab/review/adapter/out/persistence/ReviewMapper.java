package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.review.domain.model.Review;

/**
 * ReviewEntity ↔ Review 변환 유틸리티.
 */
final class ReviewMapper {

    private ReviewMapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    static Review toDomain(ReviewEntity entity) {
        return new Review(
                entity.getPublicId(), entity.getMemberPublicId(), entity.getProductPublicId(),
                entity.getRating(), entity.getContent());
    }

    static ReviewEntity toEntity(Review domain) {
        return new ReviewEntity(
                null, domain.publicId(), domain.memberPublicId(), domain.productPublicId(),
                domain.rating(), domain.content());
    }
}
