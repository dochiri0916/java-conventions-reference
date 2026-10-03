package com.dochiri.tooltestlab.cart.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "장바구니 추가 요청")
public record AddToCartRequest(
        @Schema(description = "상품 publicId", example = "01900000-0000-7000-8000-000000000001")
        @NotBlank(message = "상품 ID는 필수입니다.")
        String productPublicId,

        @Schema(description = "수량", example = "2")
        @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
        int quantity) {
}
