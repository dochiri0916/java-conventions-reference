package com.dochiri.tooltestlab.order.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "주문 생성 요청")
public record PlaceOrderRequest(
        @Schema(description = "수령인 이름", example = "홍길동") String recipientName,
        @Schema(description = "전화번호", example = "010-1234-5678") String phone,
        @Schema(description = "배송 주소", example = "서울특별시 강남구 테헤란로 123") String address,
        @Schema(description = "우편번호", example = "06164") String zipCode) {
}
