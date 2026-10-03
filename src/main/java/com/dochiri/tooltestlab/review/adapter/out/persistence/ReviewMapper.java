package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.review.domain.model.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.model.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.model.Rating;
import com.dochiri.tooltestlab.review.domain.model.Review;
import com.dochiri.tooltestlab.review.domain.model.ReviewContent;
import com.dochiri.tooltestlab.review.domain.model.ReviewId;

final class ReviewMapper {

    private ReviewMapper() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    static Review toDomain(final ReviewEntity entity) {
        return Review.restore(
                new ReviewId(entity.getReviewId()),
                new MemberReferenceId(entity.getMemberId()),
                new ProductReferenceId(entity.getProductId()),
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
