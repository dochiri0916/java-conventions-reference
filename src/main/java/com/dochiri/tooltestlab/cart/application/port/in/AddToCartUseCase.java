package com.dochiri.tooltestlab.cart.application.port.in;

/**
 * 장바구니에 상품 추가 UseCase.
 */
public interface AddToCartUseCase {
    void add(AddToCartCommand command);
}
