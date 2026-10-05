package com.dochiri.tooltestlab.cart.adapter.out.persistence.projection;

import com.dochiri.tooltestlab.cart.application.exception.ProductSnapshotNotReadyException;
import com.dochiri.tooltestlab.cart.application.port.out.ProductCatalogPort;
import com.dochiri.tooltestlab.cart.domain.vo.ProductName;
import com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;
import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.run;

@Repository
@RequiredArgsConstructor
public final class ProductSnapshotPersistenceAdapter implements ProductCatalogPort {
    private final EntityManager entityManager;

    @Override
    public ProductSnapshot findProduct(final ProductReferenceId productId) {
        return execute(() -> {
            var entity = entityManager.find(
                    ProductSnapshotEntity.class,
                    productId.value());
            if (entity == null) {
                throw new ProductSnapshotNotReadyException();
            }
            return new ProductSnapshot(productId, ProductName.of(
                    entity.getName()),
                    Money.of(
                            entity.getPriceAmount()));
        });
    }

    @Override
    public void save(final ProductSnapshot snapshot, final long sequence) {
        run(() -> {
            ProductReferenceId id = snapshot.id();
            final String key = id.value();
            final var entity = entityManager.find(
                    ProductSnapshotEntity.class,
                    key,
                    LockModeType.PESSIMISTIC_WRITE);
            upsert(entity, key, snapshot, sequence);
            entityManager.flush();
        });
    }

    private void upsert(
            final ProductSnapshotEntity entity,
            final String key,
            final ProductSnapshot snapshot,
            final long sequence) {
        if (entity == null) {
            insert(key, snapshot, sequence);
            return;
        }
        if (sequence > entity.getSequence()) {
            apply(entity, snapshot, sequence);
        }
    }

    private void insert(final String key, final ProductSnapshot snapshot, final long sequence) {
        final var created = new ProductSnapshotEntity(key);
        apply(created, snapshot, sequence);
        entityManager.persist(created);
    }

    private static void apply(
            final ProductSnapshotEntity entity,
            final ProductSnapshot snapshot,
            final long sequence) {
        final ProductName name = snapshot.name();
        final Money price = snapshot.price();
        entity.update(
                name.value(),
                price.amount(), sequence);
    }
}
