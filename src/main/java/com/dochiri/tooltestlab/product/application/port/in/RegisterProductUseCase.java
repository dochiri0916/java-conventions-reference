package com.dochiri.tooltestlab.product.application.port.in;

/**
 * 상품 등록 UseCase.
 */
@FunctionalInterface
public interface RegisterProductUseCase {
    RegisterProductResult register(RegisterProductCommand command);
}
