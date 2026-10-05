package com.dochiri.tooltestlab.review.application.port.in.result;

import java.util.List;

public record GetProductReviewsResult(String productPublicId, List<ReviewData> reviews, double averageRating) {
    public GetProductReviewsResult {
        reviews = List.copyOf(reviews);
    }

    public record ReviewData(String reviewPublicId, String memberPublicId, int rating, String content) {
    }
}
