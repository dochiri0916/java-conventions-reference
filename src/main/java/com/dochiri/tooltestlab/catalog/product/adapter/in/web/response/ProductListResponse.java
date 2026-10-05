package com.dochiri.tooltestlab.catalog.product.adapter.in.web.response;

import com.dochiri.tooltestlab.catalog.product.application.port.in.result.CategoryNameItems;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.ListProductsResult;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.ProductSummaries;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "상품 목록 응답")
public record ProductListResponse(
        @Schema(description = "상품 요약 목록") List<ProductSummary> products,
        @Schema(description = "현재 페이지 번호", example = "0") int page,
        @Schema(description = "페이지 크기", example = "20") int size,
        @Schema(description = "전체 상품 수", example = "150") long totalCount) {

    public ProductListResponse {
        products = List.copyOf(products);
    }

    public static ProductListResponse from(final ListProductsResult result) {
        final ProductSummaries summaries = result.products();
        final List<ProductSummary> items = summaries.values().stream().map(ProductSummary::from).toList();
        return new ProductListResponse(items, result.page(), result.size(), result.totalCount());
    }

    @Schema(description = "상품 요약")
    public record ProductSummary(
            @Schema(description = "상품 publicId", example = "01900000-0000-7000-8000-000000000001") String publicId,
            @Schema(description = "상품명", example = "맥북 프로 16인치") String name,
            @Schema(description = "가격 (원 단위)", example = "3690000") long priceAmount,
            @Schema(description = "재고 수량", example = "100") int stock,
            @Schema(description = "상품 상태", example = "ACTIVE") String status,
            @Schema(description = "카테고리명 목록", example = "[\"전자기기\"]") List<String> categoryNames) {

        public ProductSummary {
            categoryNames = List.copyOf(categoryNames);
        }

        public static ProductSummary from(final ListProductsResult.ProductSummary result) {
            final CategoryNameItems categoryNames = result.categoryNames();
            return new ProductSummary(result.publicId(), result.name(), result.priceAmount(), result.stock(),
                    result.status(), categoryNames.values());
        }
    }
}
