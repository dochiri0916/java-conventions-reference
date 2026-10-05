package com.dochiri.tooltestlab.cart.application.port.in.usecase;

import com.dochiri.tooltestlab.cart.application.port.in.command.RemoveFromCartCommand;

/**
 * 장바구니 상품 제거 UseCase.
 */
@FunctionalInterface
public interface RemoveFromCartUseCase {
    void remove(RemoveFromCartCommand command);
}
