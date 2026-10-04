package com.dochiri.tooltestlab.global.messaging.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class InboxEntry {
    @Id
    @Column(nullable = false, updatable = false, length = 36)
    private String eventId;
    @Column(nullable = false, updatable = false, length = 100)
    private String eventType;
    @Column(nullable = false, updatable = false)
    private Instant processedAt;

    protected InboxEntry(final String eventId, final String eventType, final Instant processedAt) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.processedAt = processedAt;
    }
}
