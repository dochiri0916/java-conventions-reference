package com.dochiri.tooltestlab.cart.application.port.in;

import com.dochiri.tooltestlab.cart.application.command.RemoveFromCartCommand;

/**
 * 장바구니 상품 제거 UseCase.
 */
public interface RemoveFromCartUseCase {
    void remove(RemoveFromCartCommand command);
}
