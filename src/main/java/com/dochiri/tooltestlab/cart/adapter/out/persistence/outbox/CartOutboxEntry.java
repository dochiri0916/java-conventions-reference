package com.dochiri.tooltestlab.cart.adapter.out.persistence.outbox;

import com.dochiri.tooltestlab.global.messaging.persistence.OutboxEntry;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "cart_outbox", indexes = @Index(name = "idx_cart_outbox_pending", columnList = "publishedAt,id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartOutboxEntry extends OutboxEntry {
    public CartOutboxEntry(final String eventId, final String eventType, final String aggregateId,
                            final String payload, final Instant occurredAt) {
        super(eventId, eventType, aggregateId, payload, occurredAt);
    }
}
