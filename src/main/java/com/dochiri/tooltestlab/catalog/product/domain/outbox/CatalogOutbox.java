package com.dochiri.tooltestlab.catalog.product.domain.outbox;

import com.dochiri.tooltestlab.global.domain.outbox.Outbox;

public final class CatalogOutbox extends Outbox {
    public CatalogOutbox(final Message message) {
        super(message);
    }

    public CatalogOutbox(final Message message, final Delivery delivery) {
        super(message, delivery);
    }

    @Override
    public String source() {
        return "catalog";
    }

    @Override
    protected CatalogOutbox withDelivery(final Delivery delivery) {
        return new CatalogOutbox(message(), delivery);
    }
}
