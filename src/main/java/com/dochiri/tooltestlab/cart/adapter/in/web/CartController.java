package com.dochiri.tooltestlab.cart.adapter.in.web;

import com.dochiri.tooltestlab.cart.adapter.in.web.request.AddToCartRequest;
import com.dochiri.tooltestlab.cart.adapter.in.web.response.GetCartResponse;
import com.dochiri.tooltestlab.cart.application.port.in.*;
import com.dochiri.tooltestlab.member.adapter.in.web.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "장바구니", description = "장바구니 조회, 추가, 삭제 API")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final GetCartUseCase getCartUseCase;
    private final AddToCartUseCase addToCartUseCase;
    private final RemoveFromCartUseCase removeFromCartUseCase;

    @Operation(summary = "장바구니 조회", description = "로그인된 회원의 장바구니를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "404", description = "장바구니가 존재하지 않음")
    })
    @GetMapping
    public ResponseEntity<GetCartResponse> get(@Parameter(hidden = true) @LoginMember String memberPublicId) {
        final GetCartResult result = getCartUseCase.getCart(memberPublicId);
        final var items = result.items().stream()
                .map(i -> new GetCartResponse.Item(
                        i.productPublicId(), i.productName(), i.quantity(), i.unitPrice(), i.totalPrice()))
                .toList();
        return ResponseEntity.ok(
                new GetCartResponse(result.publicId(), result.memberPublicId(), items, result.totalAmount()));
    }

    @Operation(summary = "장바구니 담기", description = "상품을 장바구니에 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 상품 ID 또는 수량"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "404", description = "상품 또는 장바구니가 존재하지 않음")
    })
    @PostMapping("/items")
    public ResponseEntity<Map<String, String>> add(
            @Parameter(hidden = true) @LoginMember String memberPublicId,
            @Valid @RequestBody AddToCartRequest request) {
        addToCartUseCase.add(new AddToCartCommand(
                memberPublicId, request.productPublicId(), request.quantity()));
        return ResponseEntity.ok(Map.of("message", "장바구니에 추가되었습니다."));
    }

    @Operation(summary = "장바구니 제거", description = "장바구니에서 상품을 제거합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 제거 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 상품 ID"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "404", description = "장바구니 또는 상품이 존재하지 않음")
    })
    @DeleteMapping("/items/{productPublicId}")
    public ResponseEntity<Map<String, String>> remove(
            @Parameter(hidden = true) @LoginMember String memberPublicId,
            @Parameter(description = "상품 publicId", example = "p1r2o3d4u5c6")
            @PathVariable String productPublicId) {
        removeFromCartUseCase.remove(new RemoveFromCartCommand(memberPublicId, productPublicId));
        return ResponseEntity.ok(Map.of("message", "장바구니에서 제거되었습니다."));
    }
}
