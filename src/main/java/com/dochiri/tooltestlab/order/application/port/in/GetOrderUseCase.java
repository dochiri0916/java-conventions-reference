package com.dochiri.tooltestlab.order.application.port.in;

import com.dochiri.tooltestlab.order.application.result.GetOrderResult;

/**
 * 주문 조회 UseCase.
 */
public interface GetOrderUseCase {
    GetOrderResult getOrder(String publicId);
}
