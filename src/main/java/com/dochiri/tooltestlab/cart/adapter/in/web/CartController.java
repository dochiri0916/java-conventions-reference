package com.dochiri.tooltestlab.cart.adapter.in.web;

import com.dochiri.tooltestlab.cart.adapter.in.web.request.AddToCartRequest;
import com.dochiri.tooltestlab.cart.adapter.in.web.response.AddToCartResponse;
import com.dochiri.tooltestlab.cart.adapter.in.web.response.GetCartResponse;
import com.dochiri.tooltestlab.cart.adapter.in.web.response.RemoveFromCartResponse;
import com.dochiri.tooltestlab.cart.application.port.in.query.GetCartQuery;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.AddToCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.result.GetCartResult;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.GetCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.command.RemoveFromCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.RemoveFromCartUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "장바구니", description = "장바구니 조회, 추가, 삭제 API")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public final class CartController {

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
    public ResponseEntity<GetCartResponse> get(@Parameter(hidden = true) final String memberPublicId) {
        final GetCartResult result = getCartUseCase.getCart(new GetCartQuery(memberPublicId));
        return ResponseEntity.ok(
                GetCartResponse.from(result));
    }

    @Operation(summary = "장바구니 담기", description = "상품을 장바구니에 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 추가 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 상품 ID 또는 수량"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "404", description = "상품 또는 장바구니가 존재하지 않음")
    })
    @PostMapping("/items")
    public ResponseEntity<AddToCartResponse> add(
            @Parameter(hidden = true) final String memberPublicId,
            @Valid @RequestBody final AddToCartRequest request) {
        addToCartUseCase.add(
                request.toCommand(memberPublicId));
        return ResponseEntity.ok(new AddToCartResponse("장바구니에 추가되었습니다."));
    }

    @Operation(summary = "장바구니 제거", description = "장바구니에서 상품을 제거합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "장바구니 제거 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 상품 ID"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "404", description = "장바구니 또는 상품이 존재하지 않음")
    })
    @DeleteMapping("/items/{productPublicId}")
    public ResponseEntity<RemoveFromCartResponse> remove(
            @Parameter(hidden = true) final String memberPublicId,
            @Parameter(description = "상품 publicId", example = "01900000-0000-7000-8000-000000000001")
            @PathVariable final String productPublicId) {
        removeFromCartUseCase.remove(new RemoveFromCartCommand(memberPublicId, productPublicId));
        return ResponseEntity.ok(new RemoveFromCartResponse("장바구니에서 제거되었습니다."));
    }
}
