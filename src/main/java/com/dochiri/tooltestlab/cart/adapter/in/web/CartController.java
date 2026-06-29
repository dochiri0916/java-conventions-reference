package com.dochiri.tooltestlab.cart.adapter.in.web;

import com.dochiri.tooltestlab.cart.adapter.in.web.request.AddToCartRequest;
import com.dochiri.tooltestlab.cart.adapter.in.web.response.CartResponse;
import com.dochiri.tooltestlab.cart.application.command.AddToCartCommand;
import com.dochiri.tooltestlab.cart.application.command.RemoveFromCartCommand;
import com.dochiri.tooltestlab.cart.application.port.in.AddToCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.GetCartUseCase;
import com.dochiri.tooltestlab.cart.application.port.in.RemoveFromCartUseCase;
import com.dochiri.tooltestlab.cart.application.result.GetCartResult;
import com.dochiri.tooltestlab.global.web.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "장바구니", description = "장바구니 조회, 추가, 삭제 API")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final GetCartUseCase getCartUseCase;
    private final AddToCartUseCase addToCartUseCase;
    private final RemoveFromCartUseCase removeFromCartUseCase;

    public CartController(final GetCartUseCase getCartUseCase, final AddToCartUseCase addToCartUseCase,
                          RemoveFromCartUseCase removeFromCartUseCase) {
        this.getCartUseCase = getCartUseCase;
        this.addToCartUseCase = addToCartUseCase;
        this.removeFromCartUseCase = removeFromCartUseCase;
    }

    @Operation(summary = "장바구니 조회", description = "로그인된 회원의 장바구니를 조회합니다.")
    @GetMapping
    public ResponseEntity<CartResponse> get(@LoginMember String memberPublicId) {
        final GetCartResult result = getCartUseCase.getCart(memberPublicId);
        final var items = result.items().stream()
                .map(i -> new CartResponse.Item(
                        i.productPublicId(), i.productName(), i.quantity(), i.unitPrice(), i.totalPrice()))
                .toList();
        return ResponseEntity.ok(
                new CartResponse(result.publicId(), result.memberPublicId(), items, result.totalAmount()));
    }

    @Operation(summary = "장바구니 담기", description = "상품을 장바구니에 추가합니다.")
    @PostMapping("/items")
    public ResponseEntity<Map<String, String>> add(
            @LoginMember String memberPublicId, @Valid @RequestBody AddToCartRequest request) {
        // 상품 조회와 검증은 Application Service에서 트랜잭션 내에서 처리한다.
        addToCartUseCase.add(new AddToCartCommand(
                memberPublicId, request.productPublicId(), request.quantity()));
        return ResponseEntity.ok(Map.of("message", "장바구니에 추가되었습니다."));
    }

    @Operation(summary = "장바구니 제거", description = "장바구니에서 상품을 제거합니다.")
    @DeleteMapping("/items/{productPublicId}")
    public ResponseEntity<Map<String, String>> remove(
            @LoginMember String memberPublicId,
            @Parameter(description = "상품 publicId", example = "p1r2o3d4u5c6")
            @PathVariable String productPublicId) {
        removeFromCartUseCase.remove(new RemoveFromCartCommand(memberPublicId, productPublicId));
        return ResponseEntity.ok(Map.of("message", "장바구니에서 제거되었습니다."));
    }
}
