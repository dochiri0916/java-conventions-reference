package com.dochiri.tooltestlab.cart.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "장바구니 응답")
public record CartResponse(
        @Schema(description = "장바구니 publicId", example = "c1a2r3t4i5d6") String publicId,
        @Schema(description = "회원 publicId", example = "u1v2w3x4y5z6") String memberPublicId,
        @Schema(description = "장바구니 상품 목록") List<Item> items,
        @Schema(description = "총 금액 (원 단위)", example = "7380000") long totalAmount) {

    public CartResponse {
        items = List.copyOf(items);
    }

    @Schema(description = "장바구니 상품")
    public record Item(
            @Schema(description = "상품 publicId", example = "p1r2o3d4u5c6") String productPublicId,
            @Schema(description = "상품명", example = "맥북 프로 16인치") String productName,
            @Schema(description = "수량", example = "2") int quantity,
            @Schema(description = "단가 (원 단위)", example = "3690000") long unitPrice,
            @Schema(description = "총 가격 (원 단위)", example = "7380000") long totalPrice) {
    }
}
