package com.dochiri.tooltestlab.order.adapter.in.web.request;

import com.dochiri.tooltestlab.order.application.port.in.command.PlaceOrderCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "주문 생성 요청")
public record PlaceOrderRequest(
        @Schema(description = "수령인 이름", example = "홍길동")
        @NotBlank(message = "수령인 이름은 필수입니다.")
        String recipientName,
        @Schema(description = "전화번호", example = "010-1234-5678")
        @NotBlank(message = "전화번호는 필수입니다.")
        String phone,
        @Schema(description = "배송 주소", example = "서울특별시 강남구 테헤란로 123")
        @NotBlank(message = "배송 주소는 필수입니다.")
        String address,
        @Schema(description = "우편번호", example = "06164")
        @NotBlank(message = "우편번호는 필수입니다.")
        String zipCode) {

    public PlaceOrderCommand toCommand(final String memberPublicId) {
        return new PlaceOrderCommand(memberPublicId, recipientName, phone, address, zipCode);
    }
}
