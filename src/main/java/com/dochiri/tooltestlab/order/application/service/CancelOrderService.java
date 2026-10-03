package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.order.application.exception.NotOrderOwnerException;
import com.dochiri.tooltestlab.order.application.exception.OrderNotFoundException;
import com.dochiri.tooltestlab.order.application.port.in.CancelOrderUseCase;
import com.dochiri.tooltestlab.order.application.port.out.OrderRepositoryPort;
import com.dochiri.tooltestlab.order.application.port.out.ProductStockPort;
import com.dochiri.tooltestlab.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public final class CancelOrderService implements CancelOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductStockPort productStockPort;

    @Override
    @Transactional
    public void cancel(final String memberPublicId, final String orderPublicId) {
        final Order order = orderRepositoryPort.findByPublicId(orderPublicId)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.memberId().value().equals(memberPublicId)) {
            throw new NotOrderOwnerException();
        }

        final Order cancelled = order.cancel();
        orderRepositoryPort.update(cancelled);

        cancelled.items().values().stream()
                .sorted(Comparator.comparing(line -> line.productId().value()))
                .forEach(line -> productStockPort.increase(line.productId(), line.quantity()));
    }
}
