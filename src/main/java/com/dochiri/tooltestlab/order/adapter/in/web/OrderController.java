package com.dochiri.tooltestlab.order.adapter.in.web;

import com.dochiri.tooltestlab.global.web.LoginMember;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.adapter.in.web.request.PlaceOrderRequest;
import com.dochiri.tooltestlab.order.adapter.in.web.response.OrderResponse;
import com.dochiri.tooltestlab.order.application.command.PlaceOrderCommand;
import com.dochiri.tooltestlab.order.application.exception.NotOrderOwnerException;
import com.dochiri.tooltestlab.order.application.port.in.CancelOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.in.GetOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.in.ListOrdersUseCase;
import com.dochiri.tooltestlab.order.application.port.in.PlaceOrderUseCase;
import com.dochiri.tooltestlab.order.application.result.GetOrderResult;
import com.dochiri.tooltestlab.order.application.result.ListOrdersResult;
import com.dochiri.tooltestlab.order.application.result.PlaceOrderResult;
import com.dochiri.tooltestlab.order.domain.model.ShippingAddress;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "주문", description = "주문 생성, 조회, 취소 API")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final PlaceOrderUseCase placeOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    public OrderController(final PlaceOrderUseCase placeOrderUseCase, final GetOrderUseCase getOrderUseCase,
                           ListOrdersUseCase listOrdersUseCase, final CancelOrderUseCase cancelOrderUseCase) {
        this.placeOrderUseCase = placeOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.listOrdersUseCase = listOrdersUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
    }

    @Operation(summary = "주문 생성", description = "장바구니에 담긴 상품으로 주문을 생성합니다.")
    @PostMapping
    public ResponseEntity<OrderResponse> place(@LoginMember String memberPublicId, @RequestBody PlaceOrderRequest request) {
        final ShippingAddress shippingAddress = new ShippingAddress(
                request.recipientName(), request.phone(), request.address(), request.zipCode());
        final PlaceOrderResult result = placeOrderUseCase.place(new PlaceOrderCommand(new MemberId(memberPublicId), shippingAddress));

        final var items = result.items().stream()
                .map(i -> new OrderResponse.Item(i.productPublicId(), i.productName(), i.quantity(), i.unitPrice(), (long) i.quantity() * i.unitPrice()))
                .toList();
        return ResponseEntity.ok(new OrderResponse(result.publicId(), null, result.status(), result.totalAmount(),
                request.recipientName(), request.phone(), request.address(), request.zipCode(), items));
    }

    @Operation(summary = "주문 목록 조회", description = "로그인된 회원의 주문 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> list(@LoginMember String memberPublicId) {
        final ListOrdersResult result = listOrdersUseCase.listByMember(memberPublicId);
        final var body = result.orders().stream()
                .map(o -> Map.<String, Object>of("publicId", o.publicId(), "status", o.status(), "totalAmount", o.totalAmount(), "itemCount", o.itemCount()))
                .toList();
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "주문 상세 조회", description = "주문 publicId로 주문 상세 정보를 조회합니다.")
    @GetMapping("/{publicId}")
    public ResponseEntity<OrderResponse> get(
            @LoginMember String memberPublicId,
            @Parameter(description = "주문 publicId", example = "a1b2c3d4e5f6")
            @PathVariable String publicId) {
        final GetOrderResult result = getOrderUseCase.getOrder(publicId);
        if (!result.memberPublicId().equals(memberPublicId)) {
            throw new NotOrderOwnerException();
        }
        final var items = result.items().stream()
                .map(i -> new OrderResponse.Item(i.productPublicId(), i.productName(), i.quantity(), i.unitPrice(), i.totalPrice()))
                .toList();
        return ResponseEntity.ok(new OrderResponse(result.publicId(), result.memberPublicId(), result.status(),
                result.totalAmount(), result.recipientName(), result.phone(), result.address(), result.zipCode(), items));
    }

    @Operation(summary = "주문 취소", description = "주문을 취소합니다.")
    @PostMapping("/{publicId}/cancel")
    public ResponseEntity<Map<String, String>> cancel(
            @LoginMember String memberPublicId,
            @Parameter(description = "주문 publicId", example = "a1b2c3d4e5f6")
            @PathVariable String publicId) {
        cancelOrderUseCase.cancel(memberPublicId, publicId);
        return ResponseEntity.ok(Map.of("message", "주문이 취소되었습니다."));
    }
}
