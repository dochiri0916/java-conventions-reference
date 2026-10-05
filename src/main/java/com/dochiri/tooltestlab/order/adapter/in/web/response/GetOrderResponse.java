package com.dochiri.tooltestlab.order.adapter.in.web.response;

import com.dochiri.tooltestlab.order.application.port.in.result.GetOrderResult;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "주문 조회 응답")
public record GetOrderResponse(
        @Schema(description = "주문 publicId", example = "01900000-0000-7000-8000-000000000001") String publicId,
        @Schema(description = "회원 publicId", example = "01900000-0000-7000-8000-000000000001") String memberPublicId,
        @Schema(description = "주문 상태", example = "PLACED") String status,
        @Schema(description = "총 주문 금액 (원 단위)", example = "7380000") long totalAmount,
        @Schema(description = "수령인 이름", example = "홍길동") String recipientName,
        @Schema(description = "전화번호", example = "010-1234-5678") String phone,
        @Schema(description = "배송 주소", example = "서울특별시 강남구 테헤란로 123") String address,
        @Schema(description = "우편번호", example = "06164") String zipCode,
        @Schema(description = "주문 상품 목록") List<Item> items) {

    public GetOrderResponse {
        items = List.copyOf(items);
    }

    public static GetOrderResponse from(final GetOrderResult result) {
        final var items = result.items().stream().map(Item::from).toList();
        return new GetOrderResponse(result.publicId(), result.memberPublicId(), result.status(), result.totalAmount(),
                result.recipientName(), result.phone(), result.address(), result.zipCode(), items);
    }

    @Schema(description = "주문 상품")
    public record Item(
            @Schema(description = "상품 publicId", example = "01900000-0000-7000-8000-000000000001") String productPublicId,
            @Schema(description = "상품명", example = "맥북 프로 16인치") String productName,
            @Schema(description = "수량", example = "2") int quantity,
            @Schema(description = "단가 (원 단위)", example = "3690000") long unitPrice,
            @Schema(description = "총 가격 (원 단위)", example = "7380000") long totalPrice) {

        public static Item from(final GetOrderResult.Item result) {
            return new Item(result.productPublicId(), result.productName(), result.quantity(),
                    result.unitPrice(), result.totalPrice());
        }
    }
}
