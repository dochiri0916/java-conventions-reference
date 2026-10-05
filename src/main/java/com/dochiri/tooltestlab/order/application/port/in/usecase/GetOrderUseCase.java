package com.dochiri.tooltestlab.order.application.port.in.usecase;

import com.dochiri.tooltestlab.order.application.port.in.query.GetOrderQuery;
import com.dochiri.tooltestlab.order.application.port.in.result.GetOrderResult;

/**
 * 주문 조회 UseCase.
 */
@FunctionalInterface
public interface GetOrderUseCase {
    GetOrderResult getOrder(final GetOrderQuery query);
}
