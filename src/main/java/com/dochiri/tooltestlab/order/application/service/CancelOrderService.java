package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.exception.NotOrderOwnerException;
import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.application.port.in.CancelOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.domain.event.OrderCancelled;
import com.dochiri.tooltestlab.order.domain.model.OrderCancelledItems;
import com.dochiri.tooltestlab.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class CancelOrderService implements CancelOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void cancel(final String memberPublicId, final String orderPublicId) {
        final Order order = orderRepositoryPort.findByPublicId(orderPublicId)
                .orElseThrow(() -> OrderNotFoundException.orderNotFound(orderPublicId));

        if (!order.memberId().value().equals(memberPublicId)) {
            throw NotOrderOwnerException.notOrderOwner();
        }

        final Order cancelled = order.cancel();
        orderRepositoryPort.save(cancelled);

        final List<OrderCancelled.ItemDetail> itemDetails = cancelled.items().values().stream()
                .map(i -> new OrderCancelled.ItemDetail(i.productId(), i.quantity()))
                .toList();
        eventPublisher.publishEvent(new OrderCancelled(cancelled.id(), new OrderCancelledItems(itemDetails)));
    }
}
