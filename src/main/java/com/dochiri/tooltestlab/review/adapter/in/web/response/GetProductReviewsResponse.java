package com.dochiri.tooltestlab.review.adapter.in.web.response;

import com.dochiri.tooltestlab.review.application.port.in.result.GetProductReviewsResult;
import com.dochiri.tooltestlab.review.application.port.in.result.ReviewDataItems;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "상품 리뷰 목록 응답")
public record GetProductReviewsResponse(
        @Schema(description = "상품 publicId", example = "01900000-0000-7000-8000-000000000001") String productPublicId,
        @Schema(description = "리뷰 목록") List<ReviewData> reviews,
        @Schema(description = "평균 평점", example = "4.5") double averageRating) {

    public GetProductReviewsResponse {
        reviews = List.copyOf(reviews);
    }

    public static GetProductReviewsResponse from(final GetProductReviewsResult result) {
        final ReviewDataItems reviews = result.reviews();
        final List<ReviewData> items = reviews.values().stream().map(ReviewData::from).toList();
        return new GetProductReviewsResponse(result.productPublicId(), items, result.averageRating());
    }

    @Schema(description = "리뷰 데이터")
    public record ReviewData(
            @Schema(description = "리뷰 publicId", example = "01900000-0000-7000-8000-000000000001") String publicId,
            @Schema(description = "회원 publicId", example = "01900000-0000-7000-8000-000000000001") String memberPublicId,
            @Schema(description = "평점 (1~5)", example = "5") int rating,
            @Schema(description = "리뷰 내용", example = "정말 좋은 제품입니다!") String content) {

        public static ReviewData from(final GetProductReviewsResult.ReviewData result) {
            return new ReviewData(result.reviewPublicId(), result.memberPublicId(), result.rating(), result.content());
        }
    }
}
