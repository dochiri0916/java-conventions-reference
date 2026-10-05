package com.dochiri.tooltestlab.review.adapter.in.web.request;

import com.dochiri.tooltestlab.review.application.port.in.command.WriteReviewCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "리뷰 작성 요청")
public record WriteReviewRequest(
        @Schema(description = "상품 publicId", example = "01900000-0000-7000-8000-000000000001")
        @NotBlank(message = "상품 ID는 필수입니다.")
        String productPublicId,
        @Schema(description = "평점 (1~5)", example = "5")
        @Min(value = 1, message = "평점은 1 이상이어야 합니다.")
        @Max(value = 5, message = "평점은 5 이하여야 합니다.")
        int rating,
        @Schema(description = "리뷰 내용", example = "정말 좋은 제품입니다!")
        @NotBlank(message = "리뷰 내용은 필수입니다.")
        String content) {

    public WriteReviewCommand toCommand(final String memberPublicId) {
        return new WriteReviewCommand(memberPublicId, productPublicId, rating, content);
    }
}
