package com.dochiri.tooltestlab.cart.adapter.out.persistence.projection;

import com.dochiri.tooltestlab.cart.application.exception.ProductSnapshotNotReadyException;
import com.dochiri.tooltestlab.cart.application.port.out.ProductCatalogPort;
import com.dochiri.tooltestlab.cart.domain.vo.ProductName;
import com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductSnapshotPersistenceAdapterTest {

    private final EntityManager entityManager = mock(EntityManager.class);
    private final ProductSnapshotPersistenceAdapter adapter = new ProductSnapshotPersistenceAdapter(entityManager);

    @Test
    @DisplayName("상품 스냅샷 조회의 저장소 오류는 포트 계약의 예외로 전달한다")
    void translatesReadFailure() {
        // given
        final ProductReferenceId productId = productId();
        final PersistenceException cause = new PersistenceException("storage unavailable");
        when(entityManager.find(ProductSnapshotEntity.class, productId.value())).thenThrow(cause);

        // when
        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> adapter.findProduct(productId));

        // then
        assertSame(cause, exception.getCause());
    }

    @Test
    @DisplayName("상품 스냅샷 저장 오류는 트랜잭션 커밋 전 어댑터에서 전달한다")
    void translatesFlushFailure() {
        // given
        final var snapshot = new ProductCatalogPort.ProductSnapshot(productId(), ProductName.of("상품"), Money.of(100));
        final PersistenceException cause = new PersistenceException("storage unavailable");
        doThrow(cause).when(entityManager).flush();

        // when
        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> adapter.save(snapshot, 1));

        // then
        assertSame(cause, exception.getCause());
    }

    @Test
    @DisplayName("준비되지 않은 상품 스냅샷은 기존 애플리케이션 예외를 유지한다")
    void preservesMissingSnapshotFailure() {
        // given
        final ProductReferenceId productId = productId();

        // when
        // then
        assertThrows(ProductSnapshotNotReadyException.class, () -> adapter.findProduct(productId));
    }

    private static ProductReferenceId productId() {
        return ProductReferenceId.parse("01900000-0000-7000-8000-000000000001");
    }
}
