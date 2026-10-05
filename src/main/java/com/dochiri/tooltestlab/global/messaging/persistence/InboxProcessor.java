package com.dochiri.tooltestlab.global.messaging.persistence;

import com.dochiri.tooltestlab.global.messaging.MessageEnvelope;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public final class InboxProcessor {
    private final EntityManager entityManager;
    private final Clock clock;
    private final RequiresNewTransaction transaction;
    private final List<InboxRegistration> registrations;

    /** 소비자 이름으로 등록된 Inbox 영속 구현을 찾아 처리한다. */
    public void process(final MessageEnvelope message, final String consumer, final Runnable handler) {
        final InboxRegistration registration = registrations.stream()
                .filter(candidate -> candidate.consumer().equals(consumer))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Inbox 등록이 없는 소비자입니다: " + consumer));
        process(message, registration.type(), registration.factory(), handler);
    }

    /** Inbox 표시, 로컬 변경, 후속 Outbox 기록이 한 트랜잭션으로 커밋된다. */
    public void process(final MessageEnvelope message, final Class<? extends InboxEntry> type,
                        final EntryFactory factory, final Runnable handler) {
        transaction.run(() -> {
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
