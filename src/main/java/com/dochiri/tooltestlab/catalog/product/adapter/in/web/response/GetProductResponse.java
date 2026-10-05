package com.dochiri.tooltestlab.catalog.product.adapter.in.web.response;

import com.dochiri.tooltestlab.catalog.product.application.port.in.result.GetProductResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "상품 조회 응답")
public record GetProductResponse(
        @Schema(description = "상품 publicId", example = "01900000-0000-7000-8000-000000000001") String publicId,
        @Schema(description = "상품명", example = "맥북 프로 16인치") String name,
        @Schema(description = "상품 설명", example = "Apple M4 Pro 칩 탑재") String description,
        @Schema(description = "가격 (원 단위)", example = "3690000") long priceAmount,
        @Schema(description = "재고 수량", example = "100") int stock,
        @Schema(description = "상품 상태", example = "ACTIVE") String status,
        @Schema(description = "카테고리명 목록", example = "[\"전자기기\", \"노트북\"]") List<String> categoryNames) {

    public GetProductResponse {
        categoryNames = List.copyOf(categoryNames);
    }

    public static GetProductResponse from(final GetProductResult result) {
        return new GetProductResponse(result.publicId(), result.name(), result.description(), result.priceAmount(),
                result.stock(), result.status(), result.categoryNames());
    }
}
