package com.dochiri.tooltestlab.review.adapter.out.persistence;

import com.dochiri.tooltestlab.review.domain.Review;
import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.Rating;
import com.dochiri.tooltestlab.review.domain.vo.ReviewContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewMapperTest {

    @Test
    @DisplayName("리뷰와 참조 UUID v7 및 평점과 내용을 저장하고 동일하게 복원한다")
    void mapsReview() {
        // given
        final Review review = Review.create(
                MemberReferenceId.parse("01900000-0000-7000-8000-000000000001"),
                ProductReferenceId.parse("01900000-0000-7000-8000-000000000002"),
                Rating.of(5), ReviewContent.of("리뷰 내용입니다"));

        // when
        final ReviewEntity entity = ReviewMapper.toEntity(review);
        final Review restored = ReviewMapper.toDomain(entity);

        // then
        assertEquals(7, restored.id().reviewId().version());
        assertEquals(36, entity.getPublicId().length());
        assertEquals(review.id().value(), entity.getPublicId());
        assertEquals(review.id(), restored.id());
        assertEquals(review.memberId(), restored.memberId());
        assertEquals(review.productId(), restored.productId());
        assertEquals(review.rating(), restored.rating());
        assertEquals(review.content(), restored.content());
    }
}
