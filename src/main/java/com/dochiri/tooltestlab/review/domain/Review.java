package com.dochiri.tooltestlab.review.domain;

import com.dochiri.tooltestlab.review.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.review.domain.vo.Rating;
import com.dochiri.tooltestlab.review.domain.vo.ReviewContent;
import com.dochiri.tooltestlab.review.domain.vo.ReviewId;

import static java.util.Objects.requireNonNull;

public final class Review {

    private final ReviewId id;
    private final MemberReferenceId memberId;
    private final ProductReferenceId productId;
    private final Rating rating;
    private final ReviewContent content;

    private Review(
            final ReviewId id,
            final MemberReferenceId memberId,
            final ProductReferenceId productId,
            final Rating rating,
            final ReviewContent content
    ) {
        this.id = requireNonNull(id);
        this.memberId = requireNonNull(memberId);
        this.productId = requireNonNull(productId);
        this.rating = requireNonNull(rating);
        this.content = requireNonNull(content);
    }

    public static Review create(
            final MemberReferenceId memberId,
            final ProductReferenceId productId,
            final Rating rating,
            final ReviewContent content
    ) {
        return new Review(ReviewId.generate(), memberId, productId, rating, content);
    }

    public static Review restore(
            final ReviewId id,
            final MemberReferenceId memberId,
            final ProductReferenceId productId,
            final Rating rating,
            final ReviewContent content
    ) {
        return new Review(id, memberId, productId, rating, content);
    }

    public ReviewId id() {
        return id;
    }

    public MemberReferenceId memberId() {
        return memberId;
    }

    public ProductReferenceId productId() {
        return productId;
    }

    public Rating rating() {
        return rating;
    }

    public ReviewContent content() {
        return content;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof Review review && id.equals(review.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
