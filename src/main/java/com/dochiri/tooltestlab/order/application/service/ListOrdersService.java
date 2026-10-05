package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.port.in.query.ListOrdersQuery;
import com.dochiri.tooltestlab.order.application.port.in.result.ListOrdersResult;
import com.dochiri.tooltestlab.order.application.port.in.usecase.ListOrdersUseCase;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.Order;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class ListOrdersService implements ListOrdersUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public ListOrdersResult listByMember(final ListOrdersQuery query) {
        final List<Order> orders = orderRepositoryPort.findByMemberId(MemberReferenceId.parse(query.memberPublicId()));
        final var summaries = orders.stream()
                .map(o -> new ListOrdersResult.OrderSummary(
                        o.id().value(),
                        o.status().name(),
                        o.totalAmount().amount(),
                        o.items().size()))
                .toList();
        return new ListOrdersResult(summaries);
    }
}
