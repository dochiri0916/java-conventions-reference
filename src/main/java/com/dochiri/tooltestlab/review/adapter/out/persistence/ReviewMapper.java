package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.Rating;
import com.dochiri.tooltestlab.review.domain.Review;
import com.dochiri.tooltestlab.review.domain.vo.ReviewContent;
import com.dochiri.tooltestlab.review.domain.vo.ReviewId;

final class ReviewMapper {

    private ReviewMapper() {
        throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다");
    }

    static Review toDomain(final ReviewEntity entity) {
        return Review.restore(
                ReviewId.parse(
                        entity.getPublicId()),
                MemberReferenceId.parse(
                        entity.getMemberId()),
                ProductReferenceId.parse(
                        entity.getProductId()),
                Rating.of(
                        entity.getRating()),
                ReviewContent.of(
                        entity.getContent()));
    }

    static ReviewEntity toEntity(final Review domain) {
        return ReviewEntity.create(
                domain.id()
                        .value(),
                domain.memberId()
                        .value(),
                domain.productId()
                        .value(),
                domain.rating()
                        .value(),
                domain.content()
                        .value());
    }
}
