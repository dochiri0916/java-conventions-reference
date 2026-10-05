package com.dochiri.tooltestlab.catalog.product.application.port.in.usecase;

import com.dochiri.tooltestlab.catalog.product.application.port.in.query.ListProductsQuery;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.ListProductsResult;

/**
 * 상품 목록 조회 UseCase (페이지네이션).
 */
@FunctionalInterface
public interface ListProductsUseCase {
    ListProductsResult list(final ListProductsQuery query);
}
