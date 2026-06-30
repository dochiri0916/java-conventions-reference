package com.dochiri.tooltestlab.product.application.port.in;

/**
 * 상품 목록 조회 UseCase (페이지네이션).
 */
public interface ListProductsUseCase {
    ListProductsResult list(int page, int size);
}
