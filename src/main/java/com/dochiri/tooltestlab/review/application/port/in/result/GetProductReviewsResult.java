package com.dochiri.tooltestlab.review.application.port.in.result;

public record GetProductReviewsResult(String productPublicId, ReviewDataItems reviews, double averageRating) {

    public record ReviewData(String reviewPublicId, String memberPublicId, int rating, String content) {
    }
}
