package com.dochiri.tooltestlab.catalog.application;

import com.dochiri.tooltestlab.catalog.application.port.out.event.*;
import com.dochiri.tooltestlab.catalog.domain.event.*;
import com.dochiri.tooltestlab.catalog.product.domain.event.ProductChanged;

public final class CatalogEventMapper {
    private CatalogEventMapper() {
    }

    public static ProductChangedIntegrationEvent toIntegration(final ProductChanged event) {
        final var product = event.product();
        return new ProductChangedIntegrationEvent(product.id().value(), product.name().value(),
                product.price().amount(), product.stock().value(), product.status().name());
    }

    public static StockReservedIntegrationEvent toIntegration(final StockReserved event) {
        return new StockReservedIntegrationEvent(event.orderId().value());
    }

    public static StockRejectedIntegrationEvent toIntegration(final StockRejected event) {
        return new StockRejectedIntegrationEvent(event.orderId().value(), event.reason());
    }

    public static StockReleasedIntegrationEvent toIntegration(final StockReleased event) {
        return new StockReleasedIntegrationEvent(event.orderId().value());
    }
}
