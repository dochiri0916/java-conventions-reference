package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.exception.NotOrderOwnerException;
import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.application.port.in.CancelOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.order.application.port.out.CartSnapshotPort;
import com.dochiri.tooltestlab.order.application.OrderEventMapper;
import com.dochiri.tooltestlab.order.domain.OrderStatus;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public final class CancelOrderService implements CancelOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final IntegrationEventPort integrationEventPort;
    private final CartSnapshotPort cartSnapshotPort;

    @Override
    @Transactional
    public void cancel(final String memberPublicId, final String orderPublicId) {
        final Order order = orderRepositoryPort.findByPublicIdForUpdate(orderPublicId)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.memberId().equals(MemberReferenceId.parse(memberPublicId))) {
            throw new NotOrderOwnerException();
        }

        final Order cancelled = order.cancel();
        orderRepositoryPort.update(cancelled);

        if (order.status() == OrderStatus.STOCK_PENDING) {
            cartSnapshotPort.release(order.cartId(), order.cartRevision());
        }
        integrationEventPort.publish(OrderEventMapper.toIntegration(cancelled.cancelledEvent()));
    }
}
