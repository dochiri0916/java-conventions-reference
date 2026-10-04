package com.dochiri.tooltestlab.catalog.application.service;

import com.dochiri.tooltestlab.catalog.application.CatalogEventMapper;
import com.dochiri.tooltestlab.catalog.application.port.in.*;
import com.dochiri.tooltestlab.catalog.application.port.out.*;
import com.dochiri.tooltestlab.catalog.domain.StockReservation;
import com.dochiri.tooltestlab.catalog.domain.event.StockReleased;
import com.dochiri.tooltestlab.catalog.domain.vo.OrderReferenceId;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public final class ReleaseOrderStockService implements ReleaseOrderStockUseCase {
    private final StockReservationRepositoryPort reservationRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public void release(final ReleaseOrderStockCommand command) {
        final var orderId = OrderReferenceId.parse(command.orderId());
        final var reservation = reservationRepositoryPort.findForUpdate(orderId)
                .orElse(new StockReservation(orderId, StockReservation.Status.RELEASED, List.of(), null));
        if (reservation.status() == StockReservation.Status.RESERVED) {
            reservation.lines().stream().sorted(Comparator.comparing(line -> line.productId().value())).forEach(line -> {
                final var product = productRepositoryPort.findByIdForUpdate(line.productId()).orElseThrow(ProductNotFoundException::new);
                final var changed = productRepositoryPort.update(product.increaseStock(line.quantity().value()));
                integrationEventPort.publish(CatalogEventMapper.toIntegration(changed.changedEvent()));
            });
        }
        reservationRepositoryPort.save(reservation.release());
        integrationEventPort.publish(CatalogEventMapper.toIntegration(new StockReleased(orderId)));
    }
}
