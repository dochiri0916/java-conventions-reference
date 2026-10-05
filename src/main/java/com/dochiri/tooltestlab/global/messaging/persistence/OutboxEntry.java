package com.dochiri.tooltestlab.global.messaging.persistence;

import com.dochiri.tooltestlab.global.domain.outbox.Outbox;
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

    protected OutboxEntry(final Outbox outbox) {
        final var message = outbox.message();
        this.eventId = message.eventId();
        this.eventType = message.eventType();
        this.aggregateId = message.aggregateId();
        this.payload = message.payload();
        this.occurredAt = message.occurredAt();
        update(outbox);
    }

    public abstract Outbox toDomain();

    protected Outbox.Message message() {
        return new Outbox.Message(eventId, eventType, aggregateId, id == null ? 0 : id, occurredAt, payload);
    }

    protected Outbox.Delivery delivery() {
        return new Outbox.Delivery(publishedAt, nextAttemptAt, attempts, lastError);
    }

    public void update(final Outbox outbox) {
        final var delivery = outbox.delivery();
        publishedAt = delivery.publishedAt();
        nextAttemptAt = delivery.nextAttemptAt();
        attempts = delivery.attempts();
        lastError = delivery.lastError();
    }
}
