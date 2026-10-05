package com.dochiri.tooltestlab.order.adapter.in.web;

import com.dochiri.tooltestlab.order.adapter.in.web.request.PlaceOrderRequest;
import com.dochiri.tooltestlab.order.adapter.in.web.response.CancelOrderResponse;
import com.dochiri.tooltestlab.order.adapter.in.web.response.GetOrderResponse;
import com.dochiri.tooltestlab.order.adapter.in.web.response.OrderSummaryResponse;
import com.dochiri.tooltestlab.order.adapter.in.web.response.PlaceOrderResponse;
import com.dochiri.tooltestlab.order.application.port.in.query.GetOrderQuery;
import com.dochiri.tooltestlab.order.application.port.in.query.ListOrdersQuery;
import com.dochiri.tooltestlab.order.application.port.in.usecase.CancelOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.in.result.GetOrderResult;
import com.dochiri.tooltestlab.order.application.port.in.usecase.GetOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.in.result.ListOrdersResult;
import com.dochiri.tooltestlab.order.application.port.in.usecase.ListOrdersUseCase;
import com.dochiri.tooltestlab.order.application.port.in.result.PlaceOrderResult;
import com.dochiri.tooltestlab.order.application.port.in.usecase.PlaceOrderUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "주문", description = "주문 생성, 조회, 취소 API")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public final class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    @Operation(summary = "주문 생성", description = "동기화된 장바구니로 주문을 접수합니다. PENDING 상태로 반환됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "주문 접수"),
            @ApiResponse(responseCode = "400", description = "잘못된 배송지"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "409", description = "빈 장바구니, 장바구니 동기화 대기 또는 중복 주문 요청")
    })
    @PostMapping
    public ResponseEntity<PlaceOrderResponse> place(
            @Parameter(hidden = true) final String memberPublicId,
            @Valid @RequestBody final PlaceOrderRequest request) {
        final PlaceOrderResult result = placeOrderUseCase.place(
                request.toCommand(memberPublicId));

        return ResponseEntity.accepted()
                .body(
                        PlaceOrderResponse.from(result));
    }

    @Operation(summary = "주문 목록 조회", description = "로그인된 회원의 주문 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
    })
    @GetMapping
    public ResponseEntity<List<OrderSummaryResponse>> list(
            @Parameter(hidden = true) final String memberPublicId) {
        final ListOrdersResult result = listOrdersUseCase.listByMember(new ListOrdersQuery(memberPublicId));
        final var body = result.orders().stream()
                .map(OrderSummaryResponse::from)
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
            @Parameter(hidden = true) final String memberPublicId,
            @Parameter(description = "주문 publicId", example = "01900000-0000-7000-8000-000000000001")
            @PathVariable final String publicId) {
        final GetOrderResult result = getOrderUseCase.getOrder(new GetOrderQuery(memberPublicId, publicId));
        return ResponseEntity.ok(
                GetOrderResponse.from(result));
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
    public ResponseEntity<CancelOrderResponse> cancel(
            @Parameter(hidden = true) final String memberPublicId,
            @Parameter(description = "주문 publicId", example = "01900000-0000-7000-8000-000000000001")
            @PathVariable final String publicId) {
        cancelOrderUseCase.cancel(memberPublicId, publicId);
        return ResponseEntity.ok(new CancelOrderResponse("주문이 취소되었습니다."));
    }
}
