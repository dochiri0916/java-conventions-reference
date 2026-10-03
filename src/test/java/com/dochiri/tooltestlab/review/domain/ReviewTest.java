package com.dochiri.tooltestlab.review.domain;

import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.Rating;
import com.dochiri.tooltestlab.review.domain.vo.ReviewContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    @Test
    @DisplayName("리뷰 생성과 복원은 null 필수 필드를 거부한다")
    void rejectsNullFields() {
        // given
        final MemberReferenceId memberId = MemberReferenceId.parse("01900000-0000-7000-8000-000000000001");
        final ProductReferenceId productId = ProductReferenceId.parse("01900000-0000-7000-8000-000000000002");
        final Rating rating = Rating.of(5);
        final ReviewContent content = ReviewContent.of("리뷰 내용입니다");
        final Review review = Review.create(memberId, productId, rating, content);

        // when
        // then
        assertThrows(NullPointerException.class, () -> Review.create(null, productId, rating, content));
        assertThrows(NullPointerException.class, () -> Review.create(memberId, null, rating, content));
        assertThrows(NullPointerException.class, () -> Review.create(memberId, productId, null, content));
        assertThrows(NullPointerException.class, () -> Review.create(memberId, productId, rating, null));
        assertThrows(NullPointerException.class, () -> Review.restore(null, memberId, productId, rating, content));
        assertThrows(NullPointerException.class, () -> Review.restore(review.id(), null, productId, rating, content));
        assertThrows(NullPointerException.class, () -> Review.restore(review.id(), memberId, null, rating, content));
        assertThrows(NullPointerException.class, () -> Review.restore(review.id(), memberId, productId, null, content));
        assertThrows(NullPointerException.class, () -> Review.restore(review.id(), memberId, productId, rating, null));
    }
}
