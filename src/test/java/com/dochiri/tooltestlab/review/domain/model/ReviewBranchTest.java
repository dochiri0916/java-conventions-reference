package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewBranchTest {

    @Test
    @DisplayName("create 성공")
    void create() {
        // given
        var memberId = new MemberId("m1");
        var productId = new ProductId("p1");
        var rating = new Rating(5);
        var content = new ReviewContent("좋아요");

        // when
        var review = Review.create(memberId, productId, rating, content);

        // then
        assertThat(review.id()).isNotNull();
        assertThat(review.rating().value()).isEqualTo(5);
    }

    @Test
    @DisplayName("equals - 다른 타입 false")
    void equalsDifferentType() {
        // given
        var review = Review.create(new MemberId("m1"), new ProductId("p1"), new Rating(5), new ReviewContent("좋아요"));
        var other = new Object();

        // when
        var result = review.equals(other);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("equals - 다른 인스턴스 같은 ID")
    void equalsDifferentInstanceSameId() {
        // given
        var r1 = Review.create(new MemberId("m1"), new ProductId("p1"), new Rating(5), new ReviewContent("좋아요"));
        var r2 = Review.restore(
                r1.id(), new MemberId("m2"), new ProductId("p2"), new Rating(1), new ReviewContent("별로"));

        // when
        var result = r1.equals(r2);

        // then
        assertThat(result).isTrue();
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
