package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.product.domain.model.ProductId;

import static java.util.Objects.requireNonNull;

public record Review(
        ReviewId id,
        MemberId memberId,
        ProductId productId,
        Rating rating,
        ReviewContent content
) {

    public Review {
        requireNonNull(id, "리뷰 ID는 null일 수 없습니다.");
        requireNonNull(memberId, "회원 ID는 null일 수 없습니다.");
        requireNonNull(productId, "상품 ID는 null일 수 없습니다.");
        requireNonNull(rating, "평점은 null일 수 없습니다.");
        requireNonNull(content, "리뷰 내용은 null일 수 없습니다.");
    }

    public static Review create(
            final MemberId memberId, final ProductId productId, final Rating rating, final ReviewContent content) {
        return new Review(ReviewId.generate(), memberId, productId, rating, content);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other
                || other instanceof Review review && id.equals(review.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
