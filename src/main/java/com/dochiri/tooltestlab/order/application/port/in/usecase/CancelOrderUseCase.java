package com.dochiri.tooltestlab.order.application.port.in.usecase;

/**
 * 주문 취소 UseCase.
 */
@FunctionalInterface
public interface CancelOrderUseCase {
    void cancel(String memberPublicId, String orderPublicId);
}
