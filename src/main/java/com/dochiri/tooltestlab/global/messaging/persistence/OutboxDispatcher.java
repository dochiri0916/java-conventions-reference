package com.dochiri.tooltestlab.global.messaging.persistence;

import com.dochiri.tooltestlab.global.messaging.MessageBroker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Instant;

@Component
public final class OutboxDispatcher {
    private static final Logger LOG = LoggerFactory.getLogger(OutboxDispatcher.class);
    private final EntityManager entityManager;
    private final MessageBroker broker;
    private final Clock clock;
    private final TransactionTemplate transaction;

    public OutboxDispatcher(final EntityManager entityManager, final MessageBroker broker,
                            final Clock clock, final PlatformTransactionManager transactionManager) {
        this.entityManager = entityManager;
        this.broker = broker;
        this.clock = clock;
        this.transaction = new TransactionTemplate(transactionManager);
        transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public int dispatch(final Class<? extends OutboxEntry> type, final String source) {
        int sent = 0;
        for (int i = 0; i < 100; i++) {
            final Boolean delivered = transaction.execute(status -> {
                final var entries = entityManager.createQuery(
                                "select e from " + type.getSimpleName() + " e where e.publishedAt is null order by e.id", type)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE).setMaxResults(1).getResultList();
                if (entries.isEmpty() || !entries.getFirst().ready(Instant.now(clock))) {
                    return false;
                }
                final var entry = entries.getFirst();
                try {
                    broker.send(entry.envelope(source));
                    entry.published(Instant.now(clock));
                    return true;
                } catch (final RuntimeException failure) {
                    entry.failed(Instant.now(clock), failure);
                    LOG.warn("Integration event 전송 실패. source={}, eventId={}",
                            source, entry.envelope(source).eventId(), failure);
                    return false;
                }
            });
            if (!Boolean.TRUE.equals(delivered)) {
                break;
            }
            sent++;
        }
        return sent;
    }
}
