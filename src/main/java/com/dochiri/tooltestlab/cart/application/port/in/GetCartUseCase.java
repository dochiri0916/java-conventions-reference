package com.dochiri.tooltestlab.cart.application.port.in;

/**
 * 장바구니 조회 UseCase.
 */
@FunctionalInterface
public interface GetCartUseCase {
    GetCartResult getCart(String memberPublicId);
}
