package com.dochiri.tooltestlab.review.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "리뷰 응답")
public record ReviewResponse(
        @Schema(description = "상품 publicId", example = "p1r2o3d4u5c6") String productPublicId,
        @Schema(description = "리뷰 목록") List<ReviewData> reviews,
        @Schema(description = "평균 평점", example = "4.5") double averageRating) {

    public ReviewResponse {
        reviews = List.copyOf(reviews);
    }

    @Schema(description = "리뷰 데이터")
    public record ReviewData(
            @Schema(description = "리뷰 publicId", example = "r1e2v3i4e5w6") String publicId,
            @Schema(description = "회원 publicId", example = "u1v2w3x4y5z6") String memberPublicId,
            @Schema(description = "평점 (1~5)", example = "5") int rating,
            @Schema(description = "리뷰 내용", example = "정말 좋은 제품입니다!") String content) {
    }
}
