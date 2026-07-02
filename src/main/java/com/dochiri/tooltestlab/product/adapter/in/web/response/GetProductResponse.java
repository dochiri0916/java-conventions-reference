package com.dochiri.tooltestlab.product.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "상품 조회 응답")
public record GetProductResponse(
        @Schema(description = "상품 publicId", example = "a1b2c3d4e5f6") String publicId,
        @Schema(description = "상품명", example = "맥북 프로 16인치") String name,
        @Schema(description = "상품 설명", example = "Apple M4 Pro 칩 탑재") String description,
        @Schema(description = "가격 (원 단위)", example = "3690000") long priceAmount,
        @Schema(description = "재고 수량", example = "100") int stock,
        @Schema(description = "상품 상태", example = "ACTIVE") String status,
        @Schema(description = "카테고리명 목록", example = "[\"전자기기\", \"노트북\"]") List<String> categoryNames) {

    public GetProductResponse {
        categoryNames = List.copyOf(categoryNames);
    }
}
