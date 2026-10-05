package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.OrderResultMapper;
import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.application.port.in.query.GetOrderQuery;
import com.dochiri.tooltestlab.order.application.port.in.result.GetOrderResult;
import com.dochiri.tooltestlab.order.application.port.in.usecase.GetOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.Order;
import com.dochiri.tooltestlab.order.domain.vo.OrderId;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class GetOrderService implements GetOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetOrderResult getOrder(final GetOrderQuery query) {
        final MemberReferenceId memberId = MemberReferenceId.parse(
                query.memberPublicId());
        final Order order = orderRepositoryPort.findByOrderId(
                OrderId.parse(
                        query.orderPublicId()))
                .orElseThrow(() -> new OrderNotFoundException());

        order.verifyOwner(memberId);

        return OrderResultMapper.toGetOrderResult(order);
    }
}
