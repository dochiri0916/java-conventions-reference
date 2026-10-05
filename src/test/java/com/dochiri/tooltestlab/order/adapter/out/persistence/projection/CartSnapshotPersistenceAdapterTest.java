package com.dochiri.tooltestlab.order.adapter.out.persistence.projection;

import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.global.messaging.EventCodec;
import com.dochiri.tooltestlab.order.application.exception.CheckoutAlreadyRequestedException;
import com.dochiri.tooltestlab.order.application.port.out.CartSnapshotPort;
import com.dochiri.tooltestlab.order.domain.vo.CartReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.OrderLine;
import com.dochiri.tooltestlab.order.domain.vo.ProductName;
import com.dochiri.tooltestlab.order.domain.vo.ProductReferenceId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartSnapshotPersistenceAdapterTest {

    private final EntityManager entityManager = mock(EntityManager.class);
    private final EventCodec codec = mock(EventCodec.class);
    private final CartSnapshotPersistenceAdapter adapter = new CartSnapshotPersistenceAdapter(entityManager, codec);

    @Test
    @DisplayName("장바구니 스냅샷 claim 저장 오류는 원인을 보존한 저장소 예외로 전달한다")
    void translatesClaimFlushFailure() {
        // given
        final CartSnapshotEntity entity = entity();
        final PersistenceException cause = new PersistenceException("storage unavailable");
        when(entityManager.find(CartSnapshotEntity.class, memberId().value(), LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(entity);
        when(codec.decode(entity.getItems(), CartSnapshotPersistenceAdapter.ItemsData.class))
                .thenReturn(new CartSnapshotPersistenceAdapter.ItemsData(List.of(
                        new CartSnapshotPersistenceAdapter.ItemData(productId().value(), "상품", 1, 100))));
        doThrow(cause).when(entityManager).flush();

        // when
        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> adapter.claim(memberId()));

        // then
        assertSame(cause, exception.getCause());
    }

    @Test
    @DisplayName("장바구니 스냅샷 update 저장 오류는 원인을 보존한 저장소 예외로 전달한다")
    void translatesUpdateFlushFailure() {
        // given
        final var snapshot = new CartSnapshotPort.CartSnapshot(cartId(), memberId(), 1, List.of(
                OrderLine.of(productId(), ProductName.of("상품"), Quantity.of(1), Money.of(100))));
        final PersistenceException cause = new PersistenceException("storage unavailable");
        when(codec.encode(any(CartSnapshotPersistenceAdapter.ItemsData.class))).thenReturn("items");
        doThrow(cause).when(entityManager).flush();

        // when
        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> adapter.update(snapshot, 1));

        // then
        assertSame(cause, exception.getCause());
    }

    @Test
    @DisplayName("이미 claim한 revision은 기존 체크아웃 중복 예외를 유지한다")
    void preservesAlreadyClaimedRevisionFailure() {
        // given
        final CartSnapshotEntity entity = entity();
        entity.claim();
        when(entityManager.find(CartSnapshotEntity.class, memberId().value(), LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(entity);

        // when
        // then
        assertThrows(CheckoutAlreadyRequestedException.class, () -> adapter.claim(memberId()));
        verify(entityManager, never()).flush();
        verifyNoInteractions(codec);
    }

    private static CartSnapshotEntity entity() {
        final CartSnapshotEntity entity = new CartSnapshotEntity(memberId().value());
        entity.update(cartId().value(), 1, 1, "items");
        return entity;
    }

    private static MemberReferenceId memberId() {
        return MemberReferenceId.parse("01900000-0000-7000-8000-000000000001");
    }

    private static CartReferenceId cartId() {
        return CartReferenceId.parse("01900000-0000-7000-8000-000000000002");
    }

    private static ProductReferenceId productId() {
        return ProductReferenceId.parse("01900000-0000-7000-8000-000000000003");
    }
}
