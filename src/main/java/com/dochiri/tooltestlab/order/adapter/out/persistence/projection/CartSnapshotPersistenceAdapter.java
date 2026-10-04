package com.dochiri.tooltestlab.order.adapter.out.persistence.projection;

import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.global.messaging.EventCodec;
import com.dochiri.tooltestlab.order.application.exception.CheckoutAlreadyRequestedException;
import com.dochiri.tooltestlab.order.application.port.out.CartSnapshotPort;
import com.dochiri.tooltestlab.order.domain.vo.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public final class CartSnapshotPersistenceAdapter implements CartSnapshotPort {
    private final EntityManager entityManager;
    private final EventCodec codec;

    @Override
    public Optional<CartSnapshot> claim(final MemberReferenceId memberId) {
        final var entity = entityManager.find(CartSnapshotEntity.class, memberId.value(), LockModeType.PESSIMISTIC_WRITE);
        if (entity == null) {
            return Optional.empty();
        }
        if (entity.getClaimedRevision() == entity.getRevision()) {
            throw new CheckoutAlreadyRequestedException();
        }
        entity.claim();
        final var data = codec.decode(entity.getItems(), ItemsData.class);
        return Optional.of(new CartSnapshot(CartReferenceId.parse(entity.getCartReferenceId()), memberId,
                entity.getRevision(), data.items().stream().map(item -> OrderLine.of(
                        ProductReferenceId.parse(item.productId()), ProductName.of(item.productName()),
                        Quantity.of(item.quantity()), Money.of(item.unitPrice()))).toList()));
    }

    @Override
    public void update(final CartSnapshot snapshot, final long sequence) {
        var entity = entityManager.find(CartSnapshotEntity.class, snapshot.memberId().value(), LockModeType.PESSIMISTIC_WRITE);
        final String items = codec.encode(new ItemsData(snapshot.items().stream().map(item -> new ItemData(
                item.productId().value(), item.productName().value(), item.quantity().value(), item.unitPrice().amount())).toList()));
        if (entity == null) {
            entity = new CartSnapshotEntity(snapshot.memberId().value());
            entity.update(snapshot.cartId().value(), snapshot.revision(), sequence, items);
            entityManager.persist(entity);
        } else if (sequence > entity.getSequence() && snapshot.revision() > entity.getRevision()) {
            entity.update(snapshot.cartId().value(), snapshot.revision(), sequence, items);
        }
    }

    @Override
    public void release(final CartReferenceId cartId, final long revision) {
        entityManager.createQuery("select e from CartSnapshotEntity e where e.cartReferenceId = :cartId", CartSnapshotEntity.class)
                .setParameter("cartId", cartId.value()).setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultList().stream().findFirst().ifPresent(entity -> entity.release(revision));
    }

    public record ItemsData(List<ItemData> items) {
    }

    public record ItemData(String productId, String productName, int quantity, long unitPrice) {
    }
}
