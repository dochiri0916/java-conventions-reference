package com.dochiri.tooltestlab.review.application.port.in;

public record WriteReviewResult(
        String publicId, String memberPublicId, String productPublicId, int rating, String content) {
}
