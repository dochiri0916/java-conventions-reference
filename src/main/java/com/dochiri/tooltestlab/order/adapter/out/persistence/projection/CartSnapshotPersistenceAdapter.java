package com.dochiri.tooltestlab.order.adapter.out.persistence.projection;

import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.global.messaging.EventCodec;
import com.dochiri.tooltestlab.order.application.exception.CheckoutAlreadyRequestedException;
import com.dochiri.tooltestlab.order.application.port.out.CartSnapshotLines;
import com.dochiri.tooltestlab.order.application.port.out.CartSnapshotPort;
import com.dochiri.tooltestlab.order.domain.vo.CartReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.OrderLine;
import com.dochiri.tooltestlab.order.domain.vo.ProductName;
import com.dochiri.tooltestlab.order.domain.vo.ProductReferenceId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;
import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.run;

@Repository
@RequiredArgsConstructor
public final class CartSnapshotPersistenceAdapter implements CartSnapshotPort {
    private final EntityManager entityManager;
    private final EventCodec codec;

    @Override
    public Optional<CartSnapshot> claim(final MemberReferenceId memberId) {
        return execute(() -> {
            var entity = entityManager.find(
                    CartSnapshotEntity.class,
                    memberId.value(),
                    LockModeType.PESSIMISTIC_WRITE);
            if (entity == null) {
                return Optional.empty();
            }
            if (entity.getClaimedRevision() ==
                    entity.getRevision()) {
                throw new CheckoutAlreadyRequestedException();
            }
            entity.claim();
            final var data = codec.decode(
                    entity.getItems(),
                    ItemsData.class);
            final List<OrderLine> lines = data.items().stream()
                    .map(CartSnapshotPersistenceAdapter::toOrderLine)
                    .toList();
            final CartSnapshot snapshot = new CartSnapshot(
                    CartReferenceId.parse(
                            entity.getCartReferenceId()),
                    memberId,
                    entity.getRevision(),
                    new CartSnapshotLines(lines));
            entityManager.flush();
            return Optional.of(snapshot);
        });
    }

    @Override
    public void update(final CartSnapshot snapshot, final long sequence) {
        run(() -> {
            MemberReferenceId memberId = snapshot.memberId();
            final var entity = entityManager.find(
                    CartSnapshotEntity.class,
                    memberId.value(),
                    LockModeType.PESSIMISTIC_WRITE);
            final String items = codec.encode(toItemsData(snapshot));
            upsert(entity, snapshot, sequence, items);
            entityManager.flush();
        });
    }

    private void upsert(
            final CartSnapshotEntity entity,
            final CartSnapshot snapshot,
            final long sequence,
            final String items) {
        if (entity == null) {
            insert(snapshot, sequence, items);
            return;
        }
        if (sequence > entity.getSequence() &&
                snapshot.revision() >
                        entity.getRevision()) {
            apply(entity, snapshot, sequence, items);
        }
    }

    private void insert(final CartSnapshot snapshot, final long sequence, final String items) {
        final MemberReferenceId memberId = snapshot.memberId();
        final var created = new CartSnapshotEntity(memberId.value());
        apply(created, snapshot, sequence, items);
        entityManager.persist(created);
    }

    private static void apply(
            final CartSnapshotEntity entity,
            final CartSnapshot snapshot,
            final long sequence,
            final String items) {
        final CartReferenceId cartId = snapshot.cartId();
        entity.update(
                cartId.value(),
                snapshot.revision(), sequence, items);
    }

    private static OrderLine toOrderLine(final ItemData item) {
        return OrderLine.of(
                ProductReferenceId.parse(
                        item.productId()),
                ProductName.of(
                        item.productName()),
                Quantity.of(
                        item.quantity()),
                Money.of(
                        item.unitPrice()));
    }

    private static ItemsData toItemsData(final CartSnapshot snapshot) {
        final CartSnapshotLines lines = snapshot.items();
        final List<ItemData> items = lines.values().stream()
                .map(CartSnapshotPersistenceAdapter::toItemData)
                .toList();
        return new ItemsData(items);
    }

    private static ItemData toItemData(final OrderLine item) {
        final ProductReferenceId productId = item.productId();
        final ProductName productName = item.productName();
        final Quantity quantity = item.quantity();
        final Money unitPrice = item.unitPrice();
        return new ItemData(productId.value(),
                productName.value(),
                quantity.value(),
                unitPrice.amount());
    }

    public record ItemsData(List<ItemData> items) {
    }

    public record ItemData(String productId, String productName, int quantity, long unitPrice) {
    }
}
