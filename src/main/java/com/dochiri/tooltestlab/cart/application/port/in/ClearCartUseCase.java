package com.dochiri.tooltestlab.cart.application.port.in;

/**
 * 장바구니 비우기 UseCase.
 */
@FunctionalInterface
public interface ClearCartUseCase {
    void clear(String memberPublicId);
}
