package com.dochiri.tooltestlab.order.application.port.in.usecase;

import com.dochiri.tooltestlab.order.application.port.in.query.ListOrdersQuery;
import com.dochiri.tooltestlab.order.application.port.in.result.ListOrdersResult;

/**
 * 회원별 주문 목록 조회 UseCase.
 */
@FunctionalInterface
public interface ListOrdersUseCase {
    ListOrdersResult listByMember(ListOrdersQuery query);
}
