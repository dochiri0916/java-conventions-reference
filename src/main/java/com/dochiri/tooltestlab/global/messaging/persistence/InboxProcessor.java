package com.dochiri.tooltestlab.global.messaging.persistence;

import com.dochiri.tooltestlab.global.messaging.MessageEnvelope;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Instant;

@Component
public final class InboxProcessor {
    private final EntityManager entityManager;
    private final Clock clock;
    private final TransactionTemplate transaction;

    public InboxProcessor(final EntityManager entityManager, final Clock clock,
                          final PlatformTransactionManager transactionManager) {
        this.entityManager = entityManager;
        this.clock = clock;
        this.transaction = new TransactionTemplate(transactionManager);
        transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    /** Inbox 표시, 로컬 변경, 후속 Outbox 기록이 한 트랜잭션으로 커밋된다. */
    public void process(final MessageEnvelope message, final Class<? extends InboxEntry> type,
                        final EntryFactory factory, final Runnable handler) {
        transaction.executeWithoutResult(status -> {
            if (entityManager.find(type, message.eventId()) != null) {
                return;
            }
            entityManager.persist(factory.create(message.eventId(), message.eventType(), Instant.now(clock)));
            // 동시 중복은 PK 제약으로 차단한다. 충돌은 롤백 후 전송 계층에서 재시도한다.
            entityManager.flush();
            handler.run();
        });
    }

    @FunctionalInterface
    public interface EntryFactory {
        InboxEntry create(String eventId, String eventType, Instant processedAt);
    }
}
