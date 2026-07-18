package com.dochiri.tooltestlab.catalog.product.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "상품 등록 요청")
public record RegisterProductRequest(
        @Schema(description = "상품명", example = "맥북 프로 16인치") String name,
        @Schema(description = "상품 설명", example = "Apple M4 Pro 칩 탑재") String description,
        @Schema(description = "가격 (원 단위)", example = "3690000") int priceAmount,
        @Schema(description = "재고 수량", example = "100") int stock,
        @Schema(description = "카테고리 ID 목록", example = "[\"cat01\", \"cat02\"]") List<String> categoryIds) {
    public RegisterProductRequest {
        categoryIds = categoryIds != null ? List.copyOf(categoryIds) : List.of();
    }
}
