package com.dochiri.tooltestlab.order.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 요약 응답")
public record OrderSummaryResponse(
        @Schema(description = "주문 publicId", example = "a1b2c3d4e5f6") String publicId,
        @Schema(description = "주문 상태", example = "PLACED") String status,
        @Schema(description = "총 주문 금액 (원 단위)", example = "7380000") long totalAmount,
        @Schema(description = "상품 종류 수", example = "3") int itemCount) {
}
