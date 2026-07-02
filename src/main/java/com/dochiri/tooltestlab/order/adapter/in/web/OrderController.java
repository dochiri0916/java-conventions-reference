package com.dochiri.tooltestlab.order.adapter.in.web;

import com.dochiri.tooltestlab.member.adapter.in.web.LoginMember;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.adapter.in.web.request.PlaceOrderRequest;
import com.dochiri.tooltestlab.order.adapter.in.web.response.GetOrderResponse;
import com.dochiri.tooltestlab.order.adapter.in.web.response.OrderSummaryResponse;
import com.dochiri.tooltestlab.order.adapter.in.web.response.PlaceOrderResponse;
import com.dochiri.tooltestlab.order.application.port.in.*;
import com.dochiri.tooltestlab.order.domain.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "주문", description = "주문 생성, 조회, 취소 API")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    @Operation(summary = "주문 생성", description = "장바구니에 담긴 상품으로 주문을 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 생성 성공"),
            @ApiResponse(responseCode = "400", description = "빈 장바구니 또는 잘못된 배송지"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "409", description = "재고 부족")
    })
    @PostMapping
    public ResponseEntity<PlaceOrderResponse> place(@Parameter(hidden = true) @LoginMember String memberPublicId,
                                                      @Valid @RequestBody PlaceOrderRequest request) {
        final ShippingAddress shippingAddress = new ShippingAddress(
                new RecipientName(request.recipientName()), new Phone(request.phone()),
                new StreetAddress(request.address()), new ZipCode(request.zipCode()));
        final PlaceOrderResult result = placeOrderUseCase.place(new PlaceOrderCommand(new MemberId(memberPublicId), shippingAddress));

        final var items = result.items().stream()
                .map(i -> new PlaceOrderResponse.Item(i.productPublicId(), i.productName(), i.quantity(), i.unitPrice(), i.quantity() * i.unitPrice()))
                .toList();
        return ResponseEntity.ok(new PlaceOrderResponse(result.publicId(), result.status(), result.totalAmount(),
                request.recipientName(), request.phone(), request.address(), request.zipCode(), items));
    }

    @Operation(summary = "주문 목록 조회", description = "로그인된 회원의 주문 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
    })
    @GetMapping
    public ResponseEntity<List<OrderSummaryResponse>> list(
            @Parameter(hidden = true) @LoginMember String memberPublicId) {
        final ListOrdersResult result = listOrdersUseCase.listByMember(memberPublicId);
        final var body = result.orders().stream()
                .map(o -> new OrderSummaryResponse(o.publicId(), o.status(), o.totalAmount(), o.itemCount()))
                .toList();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "주문 상세 조회", description = "주문 publicId로 주문 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 상세 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "403", description = "주문 소유자가 아님"),
            @ApiResponse(responseCode = "404", description = "주문이 존재하지 않음")
    })
    @GetMapping("/{publicId}")
    public ResponseEntity<GetOrderResponse> get(
            @Parameter(hidden = true) @LoginMember String memberPublicId,
            @Parameter(description = "주문 publicId", example = "a1b2c3d4e5f6")
            @PathVariable String publicId) {
        final GetOrderResult result = getOrderUseCase.getOrder(memberPublicId, publicId);
        final var items = result.items().stream()
                .map(i -> new GetOrderResponse.Item(i.productPublicId(), i.productName(), i.quantity(), i.unitPrice(), i.totalPrice()))
                .toList();
        return ResponseEntity.ok(new GetOrderResponse(result.publicId(), result.memberPublicId(), result.status(),
                result.totalAmount(), result.recipientName(), result.phone(), result.address(), result.zipCode(), items));
    }

    @Operation(summary = "주문 취소", description = "주문을 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 취소 성공"),
            @ApiResponse(responseCode = "400", description = "취소할 수 없는 주문 상태"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "403", description = "주문 소유자가 아님"),
            @ApiResponse(responseCode = "404", description = "주문이 존재하지 않음")
    })
    @PostMapping("/{publicId}/cancel")
    public ResponseEntity<Map<String, String>> cancel(
            @Parameter(hidden = true) @LoginMember String memberPublicId,
            @Parameter(description = "주문 publicId", example = "a1b2c3d4e5f6")
            @PathVariable String publicId) {
        cancelOrderUseCase.cancel(memberPublicId, publicId);
        return ResponseEntity.ok(Map.of("message", "주문이 취소되었습니다."));
    }
}
