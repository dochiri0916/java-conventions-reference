package com.dochiri.tooltestlab.catalog.application.port.out;

import com.dochiri.tooltestlab.catalog.domain.StockReservation;
import com.dochiri.tooltestlab.catalog.domain.vo.OrderReferenceId;
import java.util.Optional;

public interface StockReservationRepositoryPort {
    Optional<StockReservation> findForUpdate(OrderReferenceId orderId);
    void save(StockReservation reservation);
}
