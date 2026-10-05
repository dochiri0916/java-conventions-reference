package com.dochiri.tooltestlab.global.messaging.persistence;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;

/** 중복 처리 표식. JPA가 필드를 직접 읽고 쓰므로 접근자를 노출하지 않는다. */
@MappedSuperclass
@Access(AccessType.FIELD)
@SuppressWarnings("unused") // JPA field access는 정적 사용 분석에 나타나지 않는다.
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
