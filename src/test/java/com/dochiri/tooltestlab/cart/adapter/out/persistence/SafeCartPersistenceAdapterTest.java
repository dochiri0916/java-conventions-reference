package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.domain.model.Cart;
import com.dochiri.tooltestlab.member.domain.model.MemberId;
import com.dochiri.tooltestlab.order.domain.model.Quantity;
import com.dochiri.tooltestlab.catalog.product.domain.model.Money;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SafeCartPersistenceAdapterTest {

    @Mock
    private CartJpaRepository cartJpaRepository;

    @Mock
    private CartItemJpaRepository cartItemJpaRepository;

    private SafeCartPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SafeCartPersistenceAdapter(cartJpaRepository, cartItemJpaRepository);
    }

    @Test
    @DisplayName("안전 장바구니 저장은 기존 장바구니가 없으면 새 엔티티를 저장한다")
    void saveCreatesNewCartWhenMissing() {
        // given - 기존 장바구니가 조회되지 않는다
        Cart cart = cartWithItem("safeCartMember1");
        when(cartJpaRepository.findByMemberIdForUpdate(cart.memberId().value())).thenReturn(Optional.empty());
        when(cartJpaRepository.save(any(CartEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartItemJpaRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // when - 안전 어댑터로 장바구니를 저장한다
        Cart saved = adapter.save(cart);

        // then - 새 장바구니와 아이템이 저장된다
        assertThat(saved.id()).isEqualTo(cart.id());
        assertThat(saved.items().values()).hasSize(1);
        verify(cartItemJpaRepository).deleteByCartId(cart.id().value());
    }

    @Test
    @DisplayName("안전 장바구니 저장은 기존 장바구니가 있으면 해당 엔티티를 재사용한다")
    void saveReusesExistingCartWhenPresent() {
        // given - 기존 장바구니가 비관적 락으로 조회된다
        Cart cart = cartWithItem("safeCartMember2");
        CartEntity existingEntity = CartMapper.toEntity(cart);
        when(cartJpaRepository.findByMemberIdForUpdate(cart.memberId().value()))
                .thenReturn(Optional.of(existingEntity));
        when(cartJpaRepository.save(any(CartEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartItemJpaRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // when - 기존 장바구니에 새 아이템 목록을 저장한다
        Cart saved = adapter.save(cart);

        // then - 기존 장바구니 식별자를 유지하고 아이템을 갱신한다
        assertThat(saved.id()).isEqualTo(cart.id());
        assertThat(saved.items().values()).hasSize(1);
        verify(cartItemJpaRepository).deleteByCartId(cart.id().value());
    }

    @Test
    @DisplayName("안전 장바구니 저장은 동시 생성 충돌이 발생하면 한 번 재시도한다")
    void saveRetriesWhenConcurrentCreationConflicts() {
        // given - 최초 저장에서 UNIQUE 제약 충돌이 발생한다
        Cart cart = cartWithItem("safeCartMember3");
        CartEntity existingEntity = CartMapper.toEntity(cart);
        when(cartJpaRepository.findByMemberIdForUpdate(cart.memberId().value()))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(existingEntity));
        when(cartJpaRepository.save(any(CartEntity.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate cart"))
                .thenReturn(existingEntity);
        when(cartItemJpaRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        // when - 안전 어댑터로 장바구니를 저장한다
        Cart saved = adapter.save(cart);

        // then - 다시 조회한 장바구니에 아이템 저장을 완료한다
        assertThat(saved.id()).isEqualTo(cart.id());
        verify(cartJpaRepository, times(2)).findByMemberIdForUpdate(cart.memberId().value());
    }

    private static Cart cartWithItem(final String memberId) {
        return Cart.create(new MemberId(memberId))
                .addItem(
                        new ProductId("safeCartProduct"),
                        new ProductName("안전 장바구니 상품"),
                        Quantity.from(2),
                        new Money(1_000)
                );
    }
}
