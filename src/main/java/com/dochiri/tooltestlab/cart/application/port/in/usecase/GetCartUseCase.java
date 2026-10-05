package com.dochiri.tooltestlab.cart.application.port.in.usecase;

import com.dochiri.tooltestlab.cart.application.port.in.query.GetCartQuery;
import com.dochiri.tooltestlab.cart.application.port.in.result.GetCartResult;

/**
 * 장바구니 조회 UseCase.
 */
@FunctionalInterface
public interface GetCartUseCase {
    GetCartResult getCart(final GetCartQuery query);
}
