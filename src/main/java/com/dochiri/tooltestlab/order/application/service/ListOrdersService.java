package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.port.in.ListOrdersResult;
import com.dochiri.tooltestlab.order.application.port.in.ListOrdersUseCase;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.model.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public final class ListOrdersService implements ListOrdersUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    public ListOrdersService(final OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }

    @Override
    public ListOrdersResult listByMember(final String memberPublicId) {
        final List<Order> orders = orderRepositoryPort.findByMemberId(memberPublicId);
        final var summaries = orders.stream()
                .map(o -> new ListOrdersResult.OrderSummary(o.id().value(), o.status().name(), o.totalAmount().amount(), o.items().size()))
                .toList();
        return new ListOrdersResult(summaries);
    }
}
