package com.dochiri.tooltestlab.product.application.port.in;

/**
 * 상품 등록 UseCase.
 */
public interface RegisterProductUseCase {
    RegisterProductResult register(RegisterProductCommand command);
}
