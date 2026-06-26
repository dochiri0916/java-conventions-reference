package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "false", matchIfMissing = true)
class CartPersistenceAdapter implements CartRepositoryPort {

    private final CartJpaRepository cartJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;

    CartPersistenceAdapter(CartJpaRepository cartJpaRepository, CartItemJpaRepository cartItemJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
        this.cartItemJpaRepository = cartItemJpaRepository;
    }

    @Override
    @Transactional
    public Cart save(Cart cart) {
        Optional<CartEntity> existing = cartJpaRepository.findByMemberPublicId(cart.memberPublicId());
        CartEntity cartEntity;
        if (existing.isPresent()) {
            cartEntity = existing.get();
        } else {
            cartEntity = CartMapper.toEntity(cart);
        }
        CartEntity savedCart = cartJpaRepository.save(cartEntity);

        // 기존 아이템 삭제 후 새 아이템 저장 (id-only 간접 참조)
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
