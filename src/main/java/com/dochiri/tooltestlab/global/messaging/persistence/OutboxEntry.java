package com.dochiri.tooltestlab.global.messaging.persistence;

import com.dochiri.tooltestlab.global.messaging.MessageEnvelope;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class OutboxEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String eventId;
    @Column(nullable = false, updatable = false, length = 100)
    private String eventType;
    @Column(nullable = false, updatable = false, length = 36)
    private String aggregateId;
    @Lob
    @Column(nullable = false, updatable = false)
    private String payload;
    @Column(nullable = false, updatable = false)
    private Instant occurredAt;
    private Instant publishedAt;
    @Column(nullable = false)
    private Instant nextAttemptAt;
    @Column(nullable = false)
    private int attempts;
    @Column(length = 1000)
    private String lastError;

    protected OutboxEntry(final String eventId, final String eventType, final String aggregateId,
                          final String payload, final Instant occurredAt) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.occurredAt = occurredAt;
        this.nextAttemptAt = occurredAt;
    }

    public MessageEnvelope envelope(final String source) {
        return new MessageEnvelope(eventId, source, eventType, aggregateId, id, occurredAt, payload);
    }

    public boolean ready(final Instant now) {
        return !nextAttemptAt.isAfter(now);
    }

    public void published(final Instant now) {
        publishedAt = now;
        lastError = null;
    }

    public void failed(final Instant now, final RuntimeException failure) {
        attempts++;
        nextAttemptAt = now.plusSeconds(Math.min(60, 1L << Math.min(attempts - 1, 6)));
        final String error = failure.getClass().getSimpleName() + ": " + failure.getMessage();
        lastError = error.substring(0, Math.min(error.length(), 1000));
    }
}
