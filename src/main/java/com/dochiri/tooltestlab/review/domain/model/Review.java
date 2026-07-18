package com.dochiri.tooltestlab.review.domain.model;

import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.review.domain.exception.InvalidRatingException;
import com.dochiri.tooltestlab.review.domain.exception.InvalidReviewContentException;
import com.dochiri.tooltestlab.review.domain.exception.InvalidReviewIdException;

public record Review(
        ReviewId id,
        MemberId memberId,
        ProductId productId,
        Rating rating,
        ReviewContent content
) {

    public Review {
        if (id == null) {
            throw InvalidReviewIdException.required();
        }
        if (memberId == null) {
            throw InvalidMemberIdException.required();
        }
        if (productId == null) {
            throw InvalidProductIdException.required();
        }
        if (rating == null) {
            throw InvalidRatingException.required();
        }
        if (content == null) {
            throw InvalidReviewContentException.required();
        }
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
