package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.product.domain.model.ProductId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewBranchTest {

    @Test
    @DisplayName("create 성공")
    void create() {
            // given

            // when

            // then

        var review = Review.create(new MemberId("m1"), new ProductId("p1"), new Rating(5), new ReviewContent("좋아요"));
        assertThat(review.id()).isNotNull();
        assertThat(review.rating().value()).isEqualTo(5);
    }

    @Test
    @DisplayName("equals - 다른 타입 false")
    void equalsDifferentType() {
            // given

            // when

            // then

        var review = Review.create(new MemberId("m1"), new ProductId("p1"), new Rating(5), new ReviewContent("좋아요"));
        assertThat(review.equals("string")).isFalse();
    }

    @Test
    @DisplayName("equals - 다른 인스턴스 같은 ID")
    void equalsDifferentInstanceSameId() {
            // given

            // when

            // then

        var r1 = Review.create(new MemberId("m1"), new ProductId("p1"), new Rating(5), new ReviewContent("좋아요"));
        var r2 = new Review(r1.id(), new MemberId("m2"), new ProductId("p2"), new Rating(1), new ReviewContent("별로"));
        assertThat(r1.equals(r2)).isTrue();
    }

    @Test
    @DisplayName("equals - 자기 자신과 비교하면 true")
    void equalsSelf() {
            // given
        var review = Review.create(new MemberId("m1"), new ProductId("p1"), new Rating(5), new ReviewContent("좋아요"));

            // when
        var result = review.equals(review);

            // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("equals - 다른 ID를 가지면 false")
    void equalsDifferentId() {
            // given
        var r1 = Review.create(new MemberId("m1"), new ProductId("p1"), new Rating(5), new ReviewContent("좋아요"));
        var r2 = Review.create(new MemberId("m2"), new ProductId("p2"), new Rating(3), new ReviewContent("보통"));

            // when
        var result = r1.equals(r2);

            // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("hashCode는 id 기반")
    void hashCodeBasedOnId() {
            // given
        var review = Review.create(new MemberId("m1"), new ProductId("p1"), new Rating(5), new ReviewContent("좋아요"));

            // when
        var hash = review.hashCode();

            // then
        assertThat(hash).isEqualTo(review.id().hashCode());
    }
}
