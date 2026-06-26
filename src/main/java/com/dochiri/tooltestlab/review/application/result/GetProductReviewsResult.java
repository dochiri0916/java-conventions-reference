package com.dochiri.tooltestlab.review.application.result;

import java.util.List;

public record GetProductReviewsResult(String productPublicId, List<ReviewData> reviews, double averageRating) {

    public record ReviewData(String publicId, String memberPublicId, int rating, String content) {
    }
}
