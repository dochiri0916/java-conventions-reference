package com.dochiri.tooltestlab.product.adapter.in.web;

import com.dochiri.tooltestlab.product.adapter.in.web.request.RegisterProductRequest;
import com.dochiri.tooltestlab.product.adapter.in.web.response.ProductListResponse;
import com.dochiri.tooltestlab.product.adapter.in.web.response.ProductResponse;
import com.dochiri.tooltestlab.product.application.command.RegisterProductCommand;
import com.dochiri.tooltestlab.product.application.port.in.GetProductUseCase;
import com.dochiri.tooltestlab.product.application.port.in.ListProductsUseCase;
import com.dochiri.tooltestlab.product.application.port.in.RegisterProductUseCase;
import com.dochiri.tooltestlab.product.application.result.GetProductResult;
import com.dochiri.tooltestlab.product.application.result.ListProductsResult;
import com.dochiri.tooltestlab.product.application.result.RegisterProductResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "상품", description = "상품 등록, 조회, 목록 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final RegisterProductUseCase registerProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ListProductsUseCase listProductsUseCase;

    public ProductController(final RegisterProductUseCase registerProductUseCase,
                             GetProductUseCase getProductUseCase,
                             ListProductsUseCase listProductsUseCase) {
        this.registerProductUseCase = registerProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.listProductsUseCase = listProductsUseCase;
    }

    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다.")
    @PostMapping
    public ResponseEntity<ProductResponse> register(@RequestBody RegisterProductRequest request) {
        final RegisterProductResult result = registerProductUseCase.register(
                new RegisterProductCommand(request.name(), request.description(), request.priceAmount(),
                        request.stock(), request.categoryIds() != null ? request.categoryIds() : List.of()));
        return ResponseEntity.ok(new ProductResponse(
                result.publicId(), result.name(), null, result.priceAmount(),
                result.stock(), result.status(), result.categoryNames()));
    }

    @Operation(summary = "상품 목록 조회", description = "상품 목록을 페이지네이션으로 조회합니다.")
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
    @GetMapping("/{publicId}")
    public ResponseEntity<ProductResponse> get(
            @Parameter(description = "상품 publicId", example = "a1b2c3d4e5f6")
            @PathVariable String publicId) {
        final GetProductResult result = getProductUseCase.getProduct(publicId);
        return ResponseEntity.ok(new ProductResponse(
                result.publicId(), result.name(), result.description(), result.priceAmount(),
                result.stock(), result.status(), result.categoryNames()));
    }
}
