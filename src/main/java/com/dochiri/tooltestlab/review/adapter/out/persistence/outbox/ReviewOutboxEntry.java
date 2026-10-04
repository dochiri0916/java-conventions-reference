package com.dochiri.tooltestlab.review.adapter.out.persistence.outbox;

import com.dochiri.tooltestlab.global.messaging.persistence.OutboxEntry;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "review_outbox", indexes = @Index(name = "idx_review_outbox_pending", columnList = "publishedAt,id"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewOutboxEntry extends OutboxEntry {
    public ReviewOutboxEntry(final String eventId, final String eventType, final String aggregateId,
                            final String payload, final Instant occurredAt) {
        super(eventId, eventType, aggregateId, payload, occurredAt);
    }
}
