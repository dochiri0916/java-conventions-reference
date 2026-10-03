package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.review.domain.exception.MemberReferenceIdRequiredException;
import com.dochiri.tooltestlab.review.domain.exception.ProductReferenceIdRequiredException;
import com.dochiri.tooltestlab.review.domain.exception.RatingRequiredException;
import com.dochiri.tooltestlab.review.domain.exception.ReviewContentRequiredException;
import com.dochiri.tooltestlab.review.domain.exception.ReviewIdRequiredException;

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
        this.id = validated(id, new ReviewIdRequiredException());
        this.memberId = validated(memberId, new MemberReferenceIdRequiredException());
        this.productId = validated(productId, new ProductReferenceIdRequiredException());
        this.rating = validated(rating, new RatingRequiredException());
        this.content = validated(content, new ReviewContentRequiredException());
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

    public ReviewId id() { return id; }
    public MemberReferenceId memberId() { return memberId; }
    public ProductReferenceId productId() { return productId; }
    public Rating rating() { return rating; }
    public ReviewContent content() { return content; }

    private static <T> T validated(final T value, final RuntimeException exception) {
        if (value == null) {
            throw exception;
        }
        return value;
    }

    @Override
    public boolean equals(final Object other) {
        return this == other || other instanceof Review review && id.equals(review.id);
    }

    @Override
    public int hashCode() { return id.hashCode(); }
}
