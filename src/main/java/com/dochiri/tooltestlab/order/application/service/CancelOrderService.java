package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.application.port.in.usecase.CancelOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.order.application.OrderEventMapper;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.OrderId;
import com.dochiri.tooltestlab.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public final class CancelOrderService implements CancelOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public void cancel(final String memberPublicId, final String orderPublicId) {
        final MemberReferenceId memberId = MemberReferenceId.parse(memberPublicId);
        final Order order = orderRepositoryPort.findByOrderIdForUpdate(OrderId.parse(orderPublicId))
                .orElseThrow(OrderNotFoundException::new);

        final Order cancelled = order.cancel(memberId);
        orderRepositoryPort.update(cancelled);

        integrationEventPort.publish(OrderEventMapper.toIntegration(cancelled.cancelledEvent()));
    }
}
