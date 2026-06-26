package com.dochiri.tooltestlab.cart.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장바구니 추가 요청")
public record AddToCartRequest(
        @Schema(description = "상품 publicId", example = "p1r2o3d4u5c6") String productPublicId,
        @Schema(description = "수량", example = "2") int quantity) {
}
