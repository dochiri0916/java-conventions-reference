package com.dochiri.tooltestlab.cart.application.port.in;

import com.dochiri.tooltestlab.cart.application.result.GetCartResult;

/**
 * 장바구니 조회 UseCase.
 */
public interface GetCartUseCase {
    GetCartResult getCart(String memberPublicId);
}
