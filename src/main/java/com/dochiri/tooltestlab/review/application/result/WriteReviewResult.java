package com.dochiri.tooltestlab.review.application.result;

public record WriteReviewResult(
        String publicId, String memberPublicId, String productPublicId, int rating, String content) {
}
