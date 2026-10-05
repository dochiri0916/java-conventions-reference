package com.dochiri.tooltestlab.global.messaging.persistence;

import com.dochiri.tooltestlab.global.domain.outbox.Outbox;
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
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class OutboxStore {
    private final EntityManager entityManager;
    private final EventCodec codec;
    private final Clock clock;

    @Transactional(propagation = Propagation.MANDATORY)
    public void append(final IntegrationEvent event, final Function<Outbox.Message, ? extends OutboxEntry> factory) {
        entityManager.persist(factory.apply(new Outbox.Message(UuidCreator.getTimeOrderedEpoch().toString(),
                event.eventType(), event.aggregateId(), 0, Instant.now(clock), codec.encode(event))));
    }
}
