package com.dochiri.tooltestlab.global.messaging.persistence;

import com.dochiri.tooltestlab.global.configuration.properties.OutboxProperties;
import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;

/** 완료 시각을 기준으로 보관 기간이 지난 Outbox만 작은 트랜잭션으로 정리한다. */
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "messaging.outbox.cleanup.enabled", havingValue = "true", matchIfMissing = true)
public final class OutboxCleanup {
    private static final Logger LOG = LoggerFactory.getLogger(OutboxCleanup.class);
    private final EntityManager entityManager;
    private final Clock clock;
    private final RequiresNewTransaction transaction;
    private final OutboxProperties properties;

    @Scheduled(fixedDelayString = "${messaging.outbox.cleanup.interval-ms:600000}",
            initialDelayString = "${messaging.outbox.cleanup.interval-ms:600000}")
    public void cleanup() {
        final Instant cutoff = Instant.now(clock).minus(Duration.ofDays(properties.cleanup().retentionDays()));
        final var types = entityManager.getMetamodel().getEntities().stream()
                .filter(type -> OutboxEntry.class.isAssignableFrom(type.getJavaType()))
                .sorted(Comparator.comparing(EntityType::getName)).toList();
        for (final var type : types) {
            try {
                long total = 0;
                for (int batch = 0; batch < properties.cleanup().maxBatches(); batch++) {
                    final int deleted = transaction.execute(() -> deleteBatch(type.getName(), cutoff));
                    total += deleted;
                    if (deleted < properties.cleanup().batchSize()) {
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
                .setParameter("cutoff", cutoff).setMaxResults(properties.cleanup().batchSize()).getResultList();
        if (ids.isEmpty()) {
            return 0;
        }
        return entityManager.createQuery("delete from " + entityName
                        + " e where e.id in :ids and e.publishedAt < :cutoff")
                .setParameter("ids", ids).setParameter("cutoff", cutoff).executeUpdate();
    }
}
