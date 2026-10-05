package com.dochiri.tooltestlab.global.messaging;

import com.dochiri.tooltestlab.global.domain.outbox.Outbox;

import java.time.Instant;

public record MessageEnvelope(
        String eventId, String source, String eventType, String aggregateId,
        long sequence, Instant occurredAt, String payload) {
    public static MessageEnvelope from(final Outbox outbox) {
        final var message = outbox.message();
        return new MessageEnvelope(message.eventId(), outbox.source(), message.eventType(), message.aggregateId(),
                message.sequence(), message.occurredAt(), message.payload());
    }
}
