package com.dochiri.tooltestlab.catalog.product.application.port.in.usecase;

/**
 * 상품 재고 복구 UseCase.
 */
@FunctionalInterface
public interface IncreaseProductStockUseCase {
    void increase(String productPublicId, int quantity);
}
