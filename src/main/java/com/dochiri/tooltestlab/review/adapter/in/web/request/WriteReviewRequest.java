package com.dochiri.tooltestlab.review.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 작성 요청")
public record WriteReviewRequest(
        @Schema(description = "상품 publicId", example = "p1r2o3d4u5c6") String productPublicId,
        @Schema(description = "평점 (1~5)", example = "5") int rating,
        @Schema(description = "리뷰 내용", example = "정말 좋은 제품입니다!") String content) {
}
