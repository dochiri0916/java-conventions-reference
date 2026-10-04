package com.dochiri.tooltestlab.catalog.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.application.port.out.StockReservationRepositoryPort;
import com.dochiri.tooltestlab.catalog.domain.StockReservation;
import com.dochiri.tooltestlab.catalog.domain.vo.OrderReferenceId;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.global.messaging.EventCodec;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public final class StockReservationPersistenceAdapter implements StockReservationRepositoryPort {
    private final EntityManager entityManager;
    private final EventCodec codec;

    @Override
    public Optional<StockReservation> findForUpdate(final OrderReferenceId orderId) {
        final var entity = entityManager.find(StockReservationEntity.class, orderId.value(), LockModeType.PESSIMISTIC_WRITE);
        if (entity == null) {
            return Optional.empty();
        }
        final var data = codec.decode(entity.getLines(), LinesData.class);
        return Optional.of(new StockReservation(orderId, entity.getStatus(), data.lines().stream()
                .map(line -> new StockReservation.Line(ProductId.parse(line.productId()), Quantity.of(line.quantity()))).toList(),
                entity.getRejectionReason()));
    }

    @Override
    public void save(final StockReservation reservation) {
        var entity = entityManager.find(StockReservationEntity.class, reservation.orderId().value());
        final var lines = codec.encode(new LinesData(reservation.lines().stream()
                .map(line -> new LineData(line.productId().value(), line.quantity().value())).toList()));
        if (entity == null) {
            entity = new StockReservationEntity(reservation.orderId().value());
            entity.update(reservation.status(), lines, reservation.rejectionReason());
            entityManager.persist(entity);
        } else {
            entity.update(reservation.status(), lines, reservation.rejectionReason());
        }
    }

    public record LinesData(List<LineData> lines) {
    }

    public record LineData(String productId, int quantity) {
    }
}
