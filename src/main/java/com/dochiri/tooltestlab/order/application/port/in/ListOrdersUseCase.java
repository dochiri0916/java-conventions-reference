package com.dochiri.tooltestlab.order.application.port.in;

import com.dochiri.tooltestlab.order.application.result.ListOrdersResult;

/**
 * 회원별 주문 목록 조회 UseCase.
 */
public interface ListOrdersUseCase {
    ListOrdersResult listByMember(String memberPublicId);
}
