package com.dochiri.tooltestlab.catalog.product.application;

import com.dochiri.tooltestlab.catalog.product.application.port.out.event.*;
import com.dochiri.tooltestlab.catalog.product.domain.event.ProductChanged;

public final class CatalogEventMapper {
    private CatalogEventMapper() {
    }

    public static ProductChangedIntegrationEvent toIntegration(final ProductChanged event) {
        final var product = event.product();
        return new ProductChangedIntegrationEvent(product.id().value(), product.name().value(),
                product.price().amount(), product.stock().value(), product.status().name());
    }

}
