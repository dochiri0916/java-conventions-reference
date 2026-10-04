package com.dochiri.tooltestlab.catalog.adapter.out.persistence.inbox;

import com.dochiri.tooltestlab.global.messaging.persistence.InboxEntry;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "catalog_inbox")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatalogInboxEntry extends InboxEntry {
    public CatalogInboxEntry(final String eventId, final String eventType, final Instant processedAt) {
        super(eventId, eventType, processedAt);
    }
}
