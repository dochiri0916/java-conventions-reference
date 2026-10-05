package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.OrderResultMapper;
import com.dochiri.tooltestlab.order.application.port.in.query.ListOrdersQuery;
import com.dochiri.tooltestlab.order.application.port.in.result.ListOrdersResult;
import com.dochiri.tooltestlab.order.application.port.in.usecase.ListOrdersUseCase;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.application.port.out.Orders;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class ListOrdersService implements ListOrdersUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public ListOrdersResult listByMember(final ListOrdersQuery query) {
        final MemberReferenceId memberId = MemberReferenceId.parse(
                query.memberPublicId());
        final Orders orders = orderRepositoryPort.findByMemberId(memberId);
        return OrderResultMapper.toListOrdersResult(orders);
    }
}
