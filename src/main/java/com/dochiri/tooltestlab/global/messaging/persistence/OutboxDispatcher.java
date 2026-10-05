package com.dochiri.tooltestlab.global.messaging.persistence;

import com.dochiri.tooltestlab.global.messaging.MessageBroker;
import com.dochiri.tooltestlab.global.messaging.MessageEnvelope;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;

@Component
public final class OutboxDispatcher {
    private static final Logger LOG = LoggerFactory.getLogger(OutboxDispatcher.class);
    private final EntityManager entityManager;
    private final MessageBroker broker;
    private final Clock clock;
    private final TransactionTemplate transaction;
    private final long sendTimeoutMs;

    public OutboxDispatcher(final EntityManager entityManager, final MessageBroker broker,
                            final Clock clock, final PlatformTransactionManager transactionManager,
                            @Value("${messaging.outbox.send-timeout-ms:10000}") final long sendTimeoutMs) {
        if (sendTimeoutMs <= 0) {
            throw new IllegalArgumentException("Outbox 전송 제한 시간은 양수여야 합니다.");
        }
        this.entityManager = entityManager;
        this.broker = broker;
        this.clock = clock;
        this.sendTimeoutMs = sendTimeoutMs;
        this.transaction = new TransactionTemplate(transactionManager);
        transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    public int dispatch(final Class<? extends OutboxEntry> type) {
        int sent = 0;
        for (int i = 0; i < 100; i++) {
            final Boolean delivered = transaction.execute(status -> {
                final var entries = entityManager.createQuery(
                                "select e from " + type.getSimpleName() + " e where e.publishedAt is null order by e.id", type)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE).setMaxResults(1).getResultList();
                if (entries.isEmpty()) {
                    return false;
                }
                final var entry = entries.getFirst();
                final var outbox = entry.toDomain();
                if (!outbox.ready(Instant.now(clock))) {
                    return false;
                }
                try {
                    broker.send(MessageEnvelope.from(outbox)).toCompletableFuture()
                            .orTimeout(sendTimeoutMs, TimeUnit.MILLISECONDS).join();
                    entry.update(outbox.published(Instant.now(clock)));
                    return true;
                } catch (final RuntimeException failure) {
                    final Throwable cause = failure instanceof CompletionException && failure.getCause() != null
                            ? failure.getCause() : failure;
                    entry.update(outbox.failed(Instant.now(clock), cause));
                    LOG.warn("Integration event 전송 실패. source={}, eventId={}",
                            outbox.source(), outbox.message().eventId(), cause);
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
