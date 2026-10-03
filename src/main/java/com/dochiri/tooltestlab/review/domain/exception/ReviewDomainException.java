package com.dochiri.tooltestlab.review.domain.exception;

public abstract sealed class ReviewDomainException extends RuntimeException
        permits InvalidProductReferenceIdException,
        InvalidMemberReferenceIdException,
        ReviewIdRequiredException,
        InvalidReviewIdException,
        InvalidRatingException,
        RatingRequiredException,
        InvalidReviewContentException,
        ReviewContentRequiredException,
        ReviewRequiredException,
        ProductReferenceIdRequiredException,
        MemberReferenceIdRequiredException {
    protected ReviewDomainException(final String message) {
        super(message);
    }

}
