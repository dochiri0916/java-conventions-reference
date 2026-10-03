package com.dochiri.tooltestlab.catalog.product.application.port.in;

/**
 * 상품 재고 차감 UseCase.
 */
@FunctionalInterface
public interface DecreaseProductStockUseCase {
    void decrease(String productPublicId, int quantity);
}
