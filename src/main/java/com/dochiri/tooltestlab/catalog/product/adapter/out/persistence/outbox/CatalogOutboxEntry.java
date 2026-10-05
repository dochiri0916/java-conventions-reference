package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence.outbox;

import com.dochiri.tooltestlab.global.messaging.persistence.OutboxEntry;
import com.dochiri.tooltestlab.catalog.product.domain.outbox.CatalogOutbox;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "catalog_outbox", indexes = @Index(name = "idx_catalog_outbox_pending", columnList = "publishedAt,id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatalogOutboxEntry extends OutboxEntry {
    public CatalogOutboxEntry(final CatalogOutbox outbox) {
        super(outbox);
    }

    @Override
    public CatalogOutbox toDomain() {
        return new CatalogOutbox(message(), delivery());
    }
}
