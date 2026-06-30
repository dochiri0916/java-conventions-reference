package com.dochiri.tooltestlab.order.application.port.in;

/**
 * 주문 조회 UseCase.
 */
public interface GetOrderUseCase {
    GetOrderResult getOrder(String memberPublicId, String publicId);
}
