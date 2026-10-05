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
            final var entity = entityManager.find(ProductSnapshotEntity.class, productId.value());
            if (entity == null) {
                throw new ProductSnapshotNotReadyException();
            }
            return new ProductSnapshot(productId, ProductName.of(entity.getName()), Money.of(entity.getPriceAmount()));
        });
    }

    @Override
    public void save(final ProductSnapshot snapshot, final long sequence) {
        run(() -> {
            var entity = entityManager.find(ProductSnapshotEntity.class, snapshot.id().value(), LockModeType.PESSIMISTIC_WRITE);
            if (entity == null) {
                entity = new ProductSnapshotEntity(snapshot.id().value());
                entity.update(snapshot.name().value(), snapshot.price().amount(), sequence);
                entityManager.persist(entity);
            } else if (sequence > entity.getSequence()) {
                entity.update(snapshot.name().value(), snapshot.price().amount(), sequence);
            }
            entityManager.flush();
        });
    }
}
