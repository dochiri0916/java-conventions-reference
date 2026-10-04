package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.OrderEventMapper;
import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.application.port.in.*;
import com.dochiri.tooltestlab.order.application.port.out.*;
import com.dochiri.tooltestlab.order.domain.OrderStatus;
import com.dochiri.tooltestlab.order.domain.vo.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class HandleStockReservationResultService implements HandleStockReservationResultUseCase {
    private final OrderRepositoryPort orderRepositoryPort;
    private final CartSnapshotPort cartSnapshotPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public void handle(final HandleStockReservationResultCommand command) {
        final var orderId = OrderId.parse(command.orderId());
        final var order = orderRepositoryPort.findByPublicIdForUpdate(orderId.value()).orElseThrow(OrderNotFoundException::new);
        if (order.status() == OrderStatus.CANCELLED) {
            if (command.reserved()) {
                integrationEventPort.publish(OrderEventMapper.toIntegration(order.cancelledEvent()));
            }
            return;
        }
        if (order.status() != OrderStatus.STOCK_PENDING) {
            return;
        }
        if (command.reserved()) {
            final var confirmed = orderRepositoryPort.update(order.confirmStock());
            integrationEventPort.publish(OrderEventMapper.toIntegration(confirmed.confirmedEvent()));
        } else {
            final var rejected = orderRepositoryPort.update(order.rejectStock());
            cartSnapshotPort.release(order.cartId(), order.cartRevision());
            integrationEventPort.publish(OrderEventMapper.toIntegration(rejected.rejectedEvent(command.rejectionReason())));
        }
    }
}
