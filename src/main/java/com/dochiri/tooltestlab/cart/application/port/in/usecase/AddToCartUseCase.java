package com.dochiri.tooltestlab.cart.application.port.in.usecase;

import com.dochiri.tooltestlab.cart.application.port.in.command.AddToCartCommand;

/**
 * 장바구니에 상품 추가 UseCase.
 */
@FunctionalInterface
public interface AddToCartUseCase {
    void add(AddToCartCommand command);
}
