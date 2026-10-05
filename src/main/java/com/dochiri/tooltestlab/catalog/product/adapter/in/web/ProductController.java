package com.dochiri.tooltestlab.catalog.product.adapter.in.web;

import com.dochiri.tooltestlab.catalog.product.adapter.in.web.request.RegisterProductRequest;
import com.dochiri.tooltestlab.catalog.product.adapter.in.web.response.GetProductResponse;
import com.dochiri.tooltestlab.catalog.product.adapter.in.web.response.ProductListResponse;
import com.dochiri.tooltestlab.catalog.product.adapter.in.web.response.RegisterProductResponse;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductStatus;
import com.dochiri.tooltestlab.catalog.product.application.port.in.usecase.GetProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.usecase.ListProductsUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.usecase.RegisterProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.query.GetProductQuery;
import com.dochiri.tooltestlab.catalog.product.application.port.in.query.ListProductsQuery;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.GetProductResult;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.ListProductsResult;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.RegisterProductResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품", description = "상품 등록, 조회, 목록 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public final class ProductController {

    private final RegisterProductUseCase registerProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final ListProductsUseCase listProductsUseCase;

    @Operation(summary = "상품 등록", description = "새로운 상품을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 상품명, 가격, 또는 재고")
    })
    @PostMapping
    public ResponseEntity<RegisterProductResponse> register(@Valid @RequestBody final RegisterProductRequest request) {
        final RegisterProductResult result = registerProductUseCase.register(
                request.toCommand());
        return ResponseEntity.ok(
                RegisterProductResponse.from(result));
    }

    @Operation(summary = "상품 목록 조회", description = "검색 조건(이름·상태·카테고리·가격 범위)을 선택적으로 적용해 상품 목록을 페이지네이션으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 조회 조건")
    })
    @GetMapping
    public ResponseEntity<ProductListResponse> list(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page는 0 이상이어야 합니다.") final int page,
            @Parameter(description = "페이지 크기 (1~100)", example = "20")
            @RequestParam(defaultValue = "20")
            @Min(value = 1, message = "size는 1 이상이어야 합니다.")
            @Max(value = 100, message = "size는 100 이하여야 합니다.") final int size,
            @Parameter(description = "상품 이름 검색어 (부분 일치, 대소문자 무시)")
            @RequestParam(required = false) final String keyword,
            @Parameter(description = "상품 상태")
            @RequestParam(required = false) final ProductStatus status,
            @Parameter(description = "카테고리 publicId")
            @RequestParam(required = false) final String categoryId,
            @Parameter(description = "최소 가격", example = "1000")
            @RequestParam(required = false) final Long minPrice,
            @Parameter(description = "최대 가격", example = "50000")
            @RequestParam(required = false) final Long maxPrice) {
        final ListProductsResult result = listProductsUseCase.list(new ListProductsQuery(
                page, size, keyword, status == null ? null : status.name(), categoryId, minPrice, maxPrice));
        return ResponseEntity.ok(
                ProductListResponse.from(result));
    }

    @Operation(summary = "상품 상세 조회", description = "상품 publicId로 상품 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "상품이 존재하지 않음")
    })
    @GetMapping("/{publicId}")
    public ResponseEntity<GetProductResponse> get(
            @Parameter(description = "상품 publicId", example = "01900000-0000-7000-8000-000000000001")
            @PathVariable final String publicId) {
        final GetProductResult result = getProductUseCase.getProduct(new GetProductQuery(publicId));
        return ResponseEntity.ok(
                GetProductResponse.from(result));
    }
}
