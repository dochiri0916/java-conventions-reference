package com.dochiri.tooltestlab.catalog.application.service;

import com.dochiri.tooltestlab.catalog.application.CatalogEventMapper;
import com.dochiri.tooltestlab.catalog.application.port.in.*;
import com.dochiri.tooltestlab.catalog.application.port.out.*;
import com.dochiri.tooltestlab.catalog.domain.StockReservation;
import com.dochiri.tooltestlab.catalog.domain.event.StockRejected;
import com.dochiri.tooltestlab.catalog.domain.event.StockReserved;
import com.dochiri.tooltestlab.catalog.domain.vo.OrderReferenceId;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public final class ReserveOrderStockService implements ReserveOrderStockUseCase {
    private final ProductRepositoryPort productRepositoryPort;
    private final StockReservationRepositoryPort reservationRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public void reserve(final ReserveOrderStockCommand command) {
        final var orderId = OrderReferenceId.parse(command.orderId());
        final var existing = reservationRepositoryPort.findForUpdate(orderId);
        if (existing.isPresent()) {
            publishResult(existing.get());
            return;
        }

        final var quantities = new TreeMap<String, Integer>();
        command.items().forEach(item -> quantities.merge(
                ProductId.parse(item.productId()).value(), Quantity.of(item.quantity()).value(), Math::addExact));
        final var lines = quantities.entrySet().stream().map(entry -> new StockReservation.Line(
                ProductId.parse(entry.getKey()), Quantity.of(entry.getValue()))).toList();

        // 모두 잠그고 검증한 뒤 변경하여 일부 상품만 차감되는 경우를 막는다.
        final List<Product> products = new ArrayList<>();
        String rejection = null;
        for (final var line : lines) {
            final var product = productRepositoryPort.findByIdForUpdate(line.productId());
            if (product.isEmpty()) {
                rejection = "PRODUCT_NOT_FOUND";
            } else {
                products.add(product.get());
                if (product.get().stock().value() < line.quantity().value()) {
                    rejection = "STOCK_INSUFFICIENT";
                }
            }
        }
        if (rejection != null) {
            final var rejected = new StockReservation(orderId, StockReservation.Status.REJECTED, lines, rejection);
            reservationRepositoryPort.save(rejected);
            publishResult(rejected);
            return;
        }

        for (int i = 0; i < lines.size(); i++) {
            final Product changed = productRepositoryPort.update(products.get(i).decreaseStock(lines.get(i).quantity().value()));
            integrationEventPort.publish(CatalogEventMapper.toIntegration(changed.changedEvent()));
        }
        final var reserved = new StockReservation(orderId, StockReservation.Status.RESERVED, lines, null);
        reservationRepositoryPort.save(reserved);
        publishResult(reserved);
    }

    private void publishResult(final StockReservation reservation) {
        switch (reservation.status()) {
            case RESERVED -> integrationEventPort.publish(CatalogEventMapper.toIntegration(new StockReserved(reservation.orderId())));
            case REJECTED -> integrationEventPort.publish(CatalogEventMapper.toIntegration(
                    new StockRejected(reservation.orderId(), reservation.rejectionReason())));
            case RELEASED -> { } // 취소가 먼저 도착한 주문은 재고를 예약하지 않는다.
        }
    }
}
