package com.dochiri.tooltestlab.cart.application.port.in;

import com.dochiri.tooltestlab.cart.application.command.AddToCartCommand;

/**
 * 장바구니에 상품 추가 UseCase.
 */
public interface AddToCartUseCase {
    void add(AddToCartCommand command);
}
