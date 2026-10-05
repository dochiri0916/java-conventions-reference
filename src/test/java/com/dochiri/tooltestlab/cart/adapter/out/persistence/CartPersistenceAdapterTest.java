package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.application.exception.CartCreationConflictException;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.Cart;
import com.dochiri.tooltestlab.cart.domain.vo.CartItems;
import com.dochiri.tooltestlab.cart.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.cart.domain.vo.ProductName;
import com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@Import(CartPersistenceAdapterTest.AuditingConfig.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class CartPersistenceAdapterTest {

    @Autowired CartJpaRepository cartJpaRepository;
    @Autowired CartItemJpaRepository cartItemJpaRepository;
    @Autowired EntityManager entityManager;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired JdbcTemplate jdbc;

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("기존 장바구니를 저장할 때 항목은 저장된 공개 식별자를 참조한다")
    void stores_items_under_existing_public_id(final boolean safe) {
        // given
        final CartRepositoryPort adapter = adapter(safe, cartJpaRepository);
        final MemberReferenceId memberId = memberId();
        final Cart original = save(adapter, withItem(Cart.create(memberId), "기존 상품"));
        final Cart changed = withItem(Cart.create(memberId), "변경 상품");

        // when
        final Cart saved = save(adapter, changed);
        final Cart restored = new TransactionTemplate(transactionManager).execute(
                status -> adapter.findByMemberId(memberId).orElseThrow());

        // then
        assertEquals(original.id(), saved.id());
        assertNotEquals(changed.id(), saved.id());
        assertEquals(changed.items(), saved.items());
        assertEquals(saved.items(), restored.items());
        assertEquals(1L, countCarts(memberId));
        assertTrue(cartItemJpaRepository.findByCartId(changed.id().value()).isEmpty());
        assertEquals(1, cartItemJpaRepository.findByCartId(original.id().value()).size());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("회원별 생성 제약 위반만 충돌로 전달하고 실패한 트랜잭션에서 재조회하지 않는다")
    void translates_member_creation_conflict_without_retry(final boolean safe) {
        // given
        final MemberReferenceId memberId = memberId();
        save(adapter(safe, cartJpaRepository), Cart.create(memberId));
        // 다른 요청의 생성 전 조회를 재현하고 실제 DB에서 유니크 제약 위반을 발생시킨다.
        final CartJpaRepository staleRepository = mock(CartJpaRepository.class);
        if (safe) {
            when(staleRepository.findByMemberIdForUpdate(memberId.value())).thenReturn(Optional.empty());
        } else {
            when(staleRepository.findByMemberId(memberId.value())).thenReturn(Optional.empty());
        }
        when(staleRepository.saveAndFlush(any(CartEntity.class))).thenAnswer(
                invocation -> cartJpaRepository.saveAndFlush(invocation.<CartEntity>getArgument(0)));
        final CartRepositoryPort adapter = adapter(safe, staleRepository);

        // when
        final CartCreationConflictException exception = assertThrows(
                CartCreationConflictException.class, () -> save(adapter, Cart.create(memberId)));

        // then
        assertInstanceOf(DataIntegrityViolationException.class, exception.getCause());
        verify(staleRepository).saveAndFlush(any(CartEntity.class));
        if (safe) {
            verify(staleRepository).findByMemberIdForUpdate(memberId.value());
        } else {
            verify(staleRepository).findByMemberId(memberId.value());
        }
        verifyNoMoreInteractions(staleRepository);
        assertEquals(1L, countCarts(memberId));
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("장바구니 공개 식별자 중복은 생성 충돌이 아닌 저장소 오류로 전달한다")
    void translates_duplicate_public_id_as_repository_failure(final boolean safe) {
        // given
        final CartRepositoryPort adapter = adapter(safe, cartJpaRepository);
        final Cart original = save(adapter, Cart.create(memberId()));
        final MemberReferenceId otherMember = memberId();
        final Cart duplicateId = Cart.restore(original.id(), otherMember, CartItems.empty());

        // when
        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> save(adapter, duplicateId));

        // then
        assertInstanceOf(DataIntegrityViolationException.class, exception.getCause());
        assertEquals(0L, countCarts(otherMember));
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    @DisplayName("항목의 컬럼 위반은 저장소 오류로 전달하고 장바구니 생성도 롤백한다")
    void translates_item_failure_and_rolls_back_cart(final boolean safe) {
        // given
        final CartRepositoryPort adapter = adapter(safe, cartJpaRepository);
        final MemberReferenceId memberId = memberId();
        final Cart invalid = withItem(Cart.create(memberId), "a".repeat(256));

        // when
        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> save(adapter, invalid));

        // then
        assertInstanceOf(DataIntegrityViolationException.class, exception.getCause());
        assertEquals(0L, countCarts(memberId));
    }

    private CartRepositoryPort adapter(final boolean safe, final CartJpaRepository repository) {
        return safe
                ? new SafeCartPersistenceAdapter(repository, cartItemJpaRepository, entityManager)
                : new CartPersistenceAdapter(repository, cartItemJpaRepository, entityManager);
    }

    private Cart save(final CartRepositoryPort adapter, final Cart cart) {
        return new TransactionTemplate(transactionManager).execute(status -> adapter.save(cart));
    }

    private long countCarts(final MemberReferenceId memberId) {
        return jdbc.queryForObject("select count(*) from carts where member_id = ?", Long.class, memberId.value());
    }

    private static MemberReferenceId memberId() {
        return MemberReferenceId.of(UuidCreator.getTimeOrderedEpoch());
    }

    private static Cart withItem(final Cart cart, final String name) {
        return cart.addItem(ProductReferenceId.of(UuidCreator.getTimeOrderedEpoch()),
                ProductName.of(name), Quantity.of(1), Money.of(100));
    }

    @TestConfiguration
    @EnableJpaAuditing
    static class AuditingConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("test");
        }
    }
}
