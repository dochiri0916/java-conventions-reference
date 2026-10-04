package com.dochiri.tooltestlab.global.messaging.persistence;

import com.dochiri.tooltestlab.global.messaging.EventCodec;
import com.dochiri.tooltestlab.global.messaging.IntegrationEvent;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class OutboxStore {
    private final EntityManager entityManager;
    private final EventCodec codec;
    private final Clock clock;

    @Transactional(propagation = Propagation.MANDATORY)
    public void append(final IntegrationEvent event, final EntryFactory factory) {
        entityManager.persist(factory.create(UuidCreator.getTimeOrderedEpoch().toString(),
                event.eventType(), event.aggregateId(), codec.encode(event), Instant.now(clock)));
    }

    @FunctionalInterface
    public interface EntryFactory {
        OutboxEntry create(String eventId, String eventType, String aggregateId, String payload, Instant occurredAt);
    }
}
