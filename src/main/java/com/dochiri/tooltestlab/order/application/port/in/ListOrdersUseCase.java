package com.dochiri.tooltestlab.order.application.port.in;

/**
 * 회원별 주문 목록 조회 UseCase.
 */
@FunctionalInterface
public interface ListOrdersUseCase {
    ListOrdersResult listByMember(String memberPublicId);
}
