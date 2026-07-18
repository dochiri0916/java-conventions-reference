package com.dochiri.tooltestlab.catalog.product.adapter.in.web;

import com.dochiri.tooltestlab.catalog.product.adapter.in.web.request.RegisterProductRequest;
import com.dochiri.tooltestlab.catalog.product.adapter.in.web.response.GetProductResponse;
import com.dochiri.tooltestlab.catalog.product.adapter.in.web.response.ProductListResponse;
import com.dochiri.tooltestlab.catalog.product.adapter.in.web.response.RegisterProductResponse;
import com.dochiri.tooltestlab.catalog.product.application.port.in.GetProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.ListProductsUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.RegisterProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.GetProductResult;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.ListProductsResult;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.RegisterProductResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "상품", description = "상품 등록, 조회, 목록 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final RegisterProductUseCase registerProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ListProductsUseCase listProductsUseCase;

    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 상품명, 가격, 또는 재고")
    })
    @PostMapping
    public ResponseEntity<RegisterProductResponse> register(@Valid @RequestBody RegisterProductRequest request) {
        final RegisterProductResult result = registerProductUseCase.register(
                new RegisterProductCommand(request.name(), request.description(), request.priceAmount(),
                        request.stock(), request.categoryIds()));
        return ResponseEntity.ok(new RegisterProductResponse(
                result.publicId(), result.name(), null, result.priceAmount(),
                result.stock(), result.status(), result.categoryNames()));
    }

    @Operation(summary = "상품 목록 조회", description = "상품 목록을 페이지네이션으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ProductListResponse> list(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "20")
            @RequestParam(defaultValue = "20") int size) {
        final ListProductsResult result = listProductsUseCase.list(page, size);
        final var summaries = result.products().stream()
                .map(p -> new ProductListResponse.ProductSummary(
                        p.publicId(), p.name(), p.priceAmount(), p.stock(), p.status(), p.categoryNames()))
                .toList();
        return ResponseEntity.ok(new ProductListResponse(summaries, result.page(), result.size(), result.totalCount()));
    }

    @Operation(summary = "상품 상세 조회", description = "상품 publicId로 상품 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "상품이 존재하지 않음")
    })
    @GetMapping("/{publicId}")
    public ResponseEntity<GetProductResponse> get(
            @Parameter(description = "상품 publicId", example = "a1b2c3d4e5f6")
            @PathVariable String publicId) {
        final GetProductResult result = getProductUseCase.getProduct(publicId);
        return ResponseEntity.ok(new GetProductResponse(
                result.publicId(), result.name(), result.description(), result.priceAmount(),
                result.stock(), result.status(), result.categoryNames()));
    }
}
