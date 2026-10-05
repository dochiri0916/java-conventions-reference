package com.dochiri.tooltestlab.review.domain;

import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.exception.MemberReferenceIdRequiredException;
import com.dochiri.tooltestlab.review.domain.exception.NotReviewOwnerException;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.Rating;
import com.dochiri.tooltestlab.review.domain.vo.ReviewContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReviewTest {

    @Test
    @DisplayName("리뷰 삭제는 작성자만 허용하고 삭제 이벤트에 기존 식별자를 담는다")
    void authorizes_deletion_in_the_domain() {
        // given
        final MemberReferenceId owner = MemberReferenceId.parse("01900000-0000-7000-8000-000000000001");
        final MemberReferenceId anotherMember = MemberReferenceId.parse("01900000-0000-7000-8000-000000000003");
        final Review review = Review.create(owner,
                ProductReferenceId.parse("01900000-0000-7000-8000-000000000002"),
                Rating.of(5), ReviewContent.of("리뷰 내용입니다"));
        final var deleted = review.delete(owner);

        // when & then
        assertEquals(review.id(), deleted.reviewId());
        assertEquals(review.productId(), deleted.productId());
        assertThrows(NotReviewOwnerException.class, () -> review.delete(anotherMember));
        assertThrows(MemberReferenceIdRequiredException.class, () -> review.delete(null));
    }

    @Test
    @DisplayName("리뷰 생성과 복원은 null 필수 필드를 거부한다")
    void rejects_null_fields() {
        // given
        final MemberReferenceId memberId = MemberReferenceId.parse("01900000-0000-7000-8000-000000000001");
        final ProductReferenceId productId = ProductReferenceId.parse("01900000-0000-7000-8000-000000000002");
        final Rating rating = Rating.of(5);
        final ReviewContent content = ReviewContent.of("리뷰 내용입니다");
        final Review review = Review.create(memberId, productId, rating, content);

        // when & then
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
