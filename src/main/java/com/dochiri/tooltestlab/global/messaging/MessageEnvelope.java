package com.dochiri.tooltestlab.global.messaging;

import java.time.Instant;

public record MessageEnvelope(
        String eventId, String source, String eventType, String aggregateId,
        long sequence, Instant occurredAt, String payload) {
}
