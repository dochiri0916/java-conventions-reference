package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "false", matchIfMissing = true)
class CartPersistenceAdapter implements CartRepositoryPort {

    private static final Logger LOG = LoggerFactory.getLogger(CartPersistenceAdapter.class);

    private final CartJpaRepository cartJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public Cart save(final Cart cart) {
        try {
            return doSave(cart);
        } catch (DataIntegrityViolationException e) {
            // 동시에 같은 회원의 장바구니가 처음 생성된 경우 UNIQUE 제약 위반 발생.
            // 재시도: 이제는 상대 트랜잭션이 생성한 장바구니가 존재하므로 재조회하여 반영한다.
            LOG.debug("장바구니 동시 생성 감지, 재시도합니다. memberId={}", cart.memberId().value());
            return doSave(cart);
        }
    }

    private Cart doSave(final Cart cart) {
        final Optional<CartEntity> existing = cartJpaRepository.findByMemberId(cart.memberId().value());
        if (existing.isPresent()) {
            final CartEntity cartEntity = existing.get();
            final CartEntity savedCart = cartJpaRepository.save(cartEntity);
            cartItemJpaRepository.deleteByCartId(cart.id().value());
            final List<CartItemEntity> itemEntities = CartMapper.toItemEntities(cart, cart.id().value());
            final List<CartItemEntity> savedItems = cartItemJpaRepository.saveAll(itemEntities);
            return CartMapper.toDomain(savedCart, savedItems);
        }

        final CartEntity cartEntity = CartMapper.toEntity(cart);
        final CartEntity savedCart = cartJpaRepository.save(cartEntity);
        cartItemJpaRepository.deleteByCartId(cart.id().value());
        final List<CartItemEntity> itemEntities = CartMapper.toItemEntities(cart, cart.id().value());
        final List<CartItemEntity> savedItems = cartItemJpaRepository.saveAll(itemEntities);
        return CartMapper.toDomain(savedCart, savedItems);
    }

    @Override
    public Optional<Cart> findByMemberId(final String memberPublicId) {
        return cartJpaRepository.findByMemberId(memberPublicId).flatMap(cartEntity -> {
            final List<CartItemEntity> items = cartItemJpaRepository.findByCartId(cartEntity.getCartId());
            return Optional.of(CartMapper.toDomain(cartEntity, items));
        });
    }
}
