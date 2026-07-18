package com.dochiri.tooltestlab.catalog.product.application.port.in;

import com.dochiri.tooltestlab.catalog.product.application.port.in.result.ListProductsResult;

/**
 * 상품 목록 조회 UseCase (페이지네이션).
 */
@FunctionalInterface
public interface ListProductsUseCase {
    ListProductsResult list(int page, int size);
}
