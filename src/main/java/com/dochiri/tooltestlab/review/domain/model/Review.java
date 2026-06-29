package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.product.domain.model.ProductId;

import static java.util.Objects.requireNonNull;

public record Review(
        ReviewId id,
        MemberId memberId,
        ProductId productId,
        int rating,
        String content
) {

    public Review {
        requireNonNull(id, "리뷰 ID는 null일 수 없습니다.");
        requireNonNull(memberId, "회원 ID는 null일 수 없습니다.");
        requireNonNull(productId, "상품 ID는 null일 수 없습니다.");
        requireNonNull(content, "리뷰 내용은 null일 수 없습니다.");

        content = content.strip();

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("평점은 1~5 사이여야 합니다. 입력값: " + rating);
        }
        if (content.isBlank()) {
            throw new IllegalArgumentException("리뷰 내용은 빈 값일 수 없습니다.");
        }
    }

    public static Review create(MemberId memberId, ProductId productId, int rating, String content) {
        return new Review(ReviewId.generate(), memberId, productId, rating, content);
    }

    @Override
    public boolean equals(Object other) {
        return this == other
                || other instanceof Review review && id.equals(review.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
