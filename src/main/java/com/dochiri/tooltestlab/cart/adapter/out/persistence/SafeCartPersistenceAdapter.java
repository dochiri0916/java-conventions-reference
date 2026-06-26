package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 동시성 안전 모드용 CartPersistenceAdapter.
 * {@code tool-test-lab.concurrency.safe=true}일 때 활성화.
 * PESSIMISTIC_WRITE 락으로 동시 수정을 직렬화한다.
 */
@Repository
@Transactional(readOnly = true)
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "true")
class SafeCartPersistenceAdapter implements CartRepositoryPort {

    private final CartJpaRepository cartJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;

    SafeCartPersistenceAdapter(CartJpaRepository cartJpaRepository, CartItemJpaRepository cartItemJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
        this.cartItemJpaRepository = cartItemJpaRepository;
    }

    @Override
    @Transactional
    public Cart save(Cart cart) {
        // PESSIMISTIC_WRITE로 락을 잡고 조회 → 동시 수정 방지
        Optional<CartEntity> existing = cartJpaRepository.findByMemberPublicIdForUpdate(cart.memberPublicId());
        CartEntity cartEntity;
        if (existing.isPresent()) {
            cartEntity = existing.get();
        } else {
            cartEntity = CartMapper.toEntity(cart);
        }
        CartEntity savedCart = cartJpaRepository.save(cartEntity);

        cartItemJpaRepository.deleteByCartId(savedCart.getId());
        List<CartItemEntity> itemEntities = CartMapper.toItemEntities(cart, savedCart.getId());
        List<CartItemEntity> savedItems = cartItemJpaRepository.saveAll(itemEntities);

        return CartMapper.toDomain(savedCart, savedItems);
    }

    @Override
    public Optional<Cart> findByMemberId(String memberPublicId) {
        return cartJpaRepository.findByMemberPublicId(memberPublicId).flatMap(cartEntity -> {
            List<CartItemEntity> items = cartItemJpaRepository.findByCartId(cartEntity.getId());
            return Optional.of(CartMapper.toDomain(cartEntity, items));
        });
    }
}
