package com.dochiri.tooltestlab.global.messaging.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;

/** 완료 시각을 기준으로 보관 기간이 지난 Outbox만 작은 트랜잭션으로 정리한다. */
@Component
@ConditionalOnProperty(name = "messaging.outbox.cleanup.enabled", havingValue = "true", matchIfMissing = true)
public final class OutboxCleanup {
    private static final Logger LOG = LoggerFactory.getLogger(OutboxCleanup.class);
    private final EntityManager entityManager;
    private final Clock clock;
    private final TransactionTemplate transaction;
    private final Duration retention;
    private final int batchSize;
    private final int maxBatches;

    public OutboxCleanup(final EntityManager entityManager, final Clock clock,
                         final PlatformTransactionManager transactionManager,
                         @Value("${messaging.outbox.cleanup.retention-days:7}") final long retentionDays,
                         @Value("${messaging.outbox.cleanup.batch-size:1000}") final int batchSize,
                         @Value("${messaging.outbox.cleanup.max-batches:10}") final int maxBatches) {
        if (retentionDays < 0 || batchSize <= 0 || maxBatches <= 0) {
            throw new IllegalArgumentException("Outbox 보관 기간은 0일 이상, 정리 배치 크기와 횟수는 양수여야 합니다.");
        }
        this.entityManager = entityManager;
        this.clock = clock;
        this.retention = Duration.ofDays(retentionDays);
        this.batchSize = batchSize;
        this.maxBatches = maxBatches;
        this.transaction = new TransactionTemplate(transactionManager);
        transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @Scheduled(fixedDelayString = "${messaging.outbox.cleanup.interval-ms:600000}",
            initialDelayString = "${messaging.outbox.cleanup.interval-ms:600000}")
    public void cleanup() {
        final Instant cutoff = Instant.now(clock).minus(retention);
        final var types = entityManager.getMetamodel().getEntities().stream()
                .filter(type -> OutboxEntry.class.isAssignableFrom(type.getJavaType()))
                .sorted(Comparator.comparing(EntityType::getName)).toList();
        for (final var type : types) {
            try {
                long total = 0;
                for (int batch = 0; batch < maxBatches; batch++) {
                    final int deleted = transaction.execute(status -> deleteBatch(type.getName(), cutoff));
                    total += deleted;
                    if (deleted < batchSize) {
                        break;
                    }
                }
                if (total > 0) {
                    LOG.info("완료 Outbox 정리. entity={}, deleted={}", type.getName(), total);
                }
            } catch (final RuntimeException failure) {
                LOG.warn("완료 Outbox 정리 실패. entity={}", type.getName(), failure);
            }
        }
    }

    private int deleteBatch(final String entityName, final Instant cutoff) {
        final var ids = entityManager.createQuery("select e.id from " + entityName
                        + " e where e.publishedAt < :cutoff order by e.publishedAt, e.id", Long.class)
                .setParameter("cutoff", cutoff).setMaxResults(batchSize).getResultList();
        if (ids.isEmpty()) {
            return 0;
        }
        return entityManager.createQuery("delete from " + entityName
                        + " e where e.id in :ids and e.publishedAt < :cutoff")
                .setParameter("ids", ids).setParameter("cutoff", cutoff).executeUpdate();
    }
}
