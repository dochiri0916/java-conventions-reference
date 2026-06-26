package com.dochiri.tooltestlab.order.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "주문 응답")
public record OrderResponse(
        @Schema(description = "주문 publicId", example = "a1b2c3d4e5f6") String publicId,
        @Schema(description = "회원 publicId", example = "u1v2w3x4y5z6") String memberPublicId,
        @Schema(description = "주문 상태", example = "PLACED") String status,
        @Schema(description = "총 주문 금액 (원 단위)", example = "7380000") long totalAmount,
        @Schema(description = "수령인 이름", example = "홍길동") String recipientName,
        @Schema(description = "전화번호", example = "010-1234-5678") String phone,
        @Schema(description = "배송 주소", example = "서울특별시 강남구 테헤란로 123") String address,
        @Schema(description = "우편번호", example = "06164") String zipCode,
        @Schema(description = "주문 상품 목록") List<Item> items) {

    @Schema(description = "주문 상품")
    public record Item(
            @Schema(description = "상품 publicId", example = "p1r2o3d4u5c6") String productPublicId,
            @Schema(description = "상품명", example = "맥북 프로 16인치") String productName,
            @Schema(description = "수량", example = "2") int quantity,
            @Schema(description = "단가 (원 단위)", example = "3690000") long unitPrice,
            @Schema(description = "총 가격 (원 단위)", example = "7380000") long totalPrice) {
    }
}
