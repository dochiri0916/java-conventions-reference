package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.member.domain.model.MemberId;

import static java.util.Objects.requireNonNull;

public final class Review {

    private final ReviewId id;
    private final MemberId memberId;
    private final ProductId productId;
    private final Rating rating;
    private final ReviewContent content;

    private Review(
            final ReviewId id,
            final MemberId memberId,
            final ProductId productId,
            final Rating rating,
            final ReviewContent content
    ) {
        this.id = requireNonNull(id, "리뷰 ID는 null일 수 없습니다.");
        this.memberId = requireNonNull(memberId, "회원 ID는 null일 수 없습니다.");
        this.productId = requireNonNull(productId, "상품 ID는 null일 수 없습니다.");
        this.rating = requireNonNull(rating, "평점은 null일 수 없습니다.");
        this.content = requireNonNull(content, "리뷰 내용은 null일 수 없습니다.");
    }

    public static Review create(
            final MemberId memberId,
            final ProductId productId,
            final Rating rating,
            final ReviewContent content
    ) {
        return new Review(ReviewId.generate(), memberId, productId, rating, content);
    }

    public static Review restore(
            final ReviewId id,
            final MemberId memberId,
            final ProductId productId,
            final Rating rating,
            final ReviewContent content
    ) {
        return new Review(id, memberId, productId, rating, content);
    }

    public ReviewId id() { return id; }
    public MemberId memberId() { return memberId; }
    public ProductId productId() { return productId; }
    public Rating rating() { return rating; }
    public ReviewContent content() { return content; }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof Review review && id.equals(review.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
