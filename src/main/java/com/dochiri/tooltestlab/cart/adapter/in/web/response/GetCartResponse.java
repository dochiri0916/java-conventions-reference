package com.dochiri.tooltestlab.cart.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "장바구니 조회 응답")
public record GetCartResponse(
        @Schema(description = "장바구니 publicId", example = "01900000-0000-7000-8000-000000000001") String publicId,
        @Schema(description = "회원 publicId", example = "01900000-0000-7000-8000-000000000001") String memberPublicId,
        @Schema(description = "장바구니 상품 목록") List<Item> items,
        @Schema(description = "총 금액 (원 단위)", example = "7380000") long totalAmount) {

    public GetCartResponse {
        items = List.copyOf(items);
    }

    @Schema(description = "장바구니 상품")
    public record Item(
            @Schema(description = "상품 publicId", example = "01900000-0000-7000-8000-000000000001") String productPublicId,
            @Schema(description = "상품명", example = "맥북 프로 16인치") String productName,
            @Schema(description = "수량", example = "2") int quantity,
            @Schema(description = "단가 (원 단위)", example = "3690000") long unitPrice,
            @Schema(description = "총 가격 (원 단위)", example = "7380000") long totalPrice) {
    }
}
