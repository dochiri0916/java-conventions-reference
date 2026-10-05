package com.dochiri.tooltestlab.catalog.product.application.port.in.query;

/**
 * 상품 목록 조회 조건. 검색 항목(keyword~maxPrice)은 null이면 조건에서 제외한다.
 */
public record ListProductsQuery(
        int page,
        int size,
        String keyword,
        String status,
        String categoryId,
        Long minPrice,
        Long maxPrice
) {
}
