package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.model.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "false", matchIfMissing = true)
class CartPersistenceAdapter implements CartRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(CartPersistenceAdapter.class);

    private final CartJpaRepository cartJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;

    CartPersistenceAdapter(final CartJpaRepository cartJpaRepository, final CartItemJpaRepository cartItemJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
        this.cartItemJpaRepository = cartItemJpaRepository;
    }

    @Override
    @Transactional
    public Cart save(final Cart cart) {
        try {
            return doSave(cart);
        } catch (DataIntegrityViolationException e) {
            // 동시에 같은 회원의 장바구니가 처음 생성된 경우 UNIQUE 제약 위반 발생.
            // 재시도: 이제는 상대 트랜잭션이 생성한 장바구니가 존재하므로 재조회하여 반영한다.
            log.debug("장바구니 동시 생성 감지, 재시도합니다. memberPublicId={}", cart.memberId().value());
            return doSave(cart);
        }
    }

    private Cart doSave(final Cart cart) {
        final Optional<CartEntity> existing = cartJpaRepository.findByMemberPublicId(cart.memberId().value());
        final CartEntity cartEntity;
        if (existing.isPresent()) {
            cartEntity = existing.get();
        } else {
            cartEntity = CartMapper.toEntity(cart);
        }
        final CartEntity savedCart = cartJpaRepository.save(cartEntity);

        // 기존 아이템 삭제 후 새 아이템 저장 (id-only 간접 참조)
        cartItemJpaRepository.deleteByCartId(savedCart.getId());
        final List<CartItemEntity> itemEntities = CartMapper.toItemEntities(cart, savedCart.getId());
        final List<CartItemEntity> savedItems = cartItemJpaRepository.saveAll(itemEntities);

        return CartMapper.toDomain(savedCart, savedItems);
    }

    @Override
    public Optional<Cart> findByMemberId(final String memberPublicId) {
        return cartJpaRepository.findByMemberPublicId(memberPublicId).flatMap(cartEntity -> {
            final List<CartItemEntity> items = cartItemJpaRepository.findByCartId(cartEntity.getId());
            return Optional.of(CartMapper.toDomain(cartEntity, items));
        });
    }
}
