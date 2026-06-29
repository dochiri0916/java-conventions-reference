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

/**
 * 동시성 안전 모드용 CartPersistenceAdapter.
 * {@code tool-test-lab.concurrency.safe=true}일 때 활성화.
 * PESSIMISTIC_WRITE 락으로 동시 수정을 직렬화한다.
 * <p>
 * PESSIMISTIC_WRITE 락은 기존 row에만 적용되므로, 최초 생성 시에는
 * UNIQUE 제약 위반이 발생할 수 있다. 이 경우 한 번 재시도한다.
 */
@Repository
@Transactional(readOnly = true)
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "true")
class SafeCartPersistenceAdapter implements CartRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(SafeCartPersistenceAdapter.class);

    private final CartJpaRepository cartJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;

    SafeCartPersistenceAdapter(final CartJpaRepository cartJpaRepository, final CartItemJpaRepository cartItemJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
        this.cartItemJpaRepository = cartItemJpaRepository;
    }

    @Override
    @Transactional
    public Cart save(final Cart cart) {
        try {
            return doSave(cart);
        } catch (DataIntegrityViolationException e) {
            // PESSIMISTIC_WRITE 락은 기존 row에만 적용되므로
            // 동시에 최초 생성되는 경우 UNIQUE 제약 위반이 발생할 수 있다.
            // 재시도: 이제는 상대 트랜잭션이 생성한 장바구니가 존재하므로
            // PESSIMISTIC_WRITE 락을 잡고 재조회할 수 있다.
            log.debug("장바구니 동시 생성 감지, 재시도합니다. memberPublicId={}", cart.memberId().value());
            return doSave(cart);
        }
    }

    private Cart doSave(final Cart cart) {
        // PESSIMISTIC_WRITE로 락을 잡고 조회 → 동시 수정 방지
        final Optional<CartEntity> existing = cartJpaRepository.findByMemberPublicIdForUpdate(cart.memberId().value());
        final CartEntity cartEntity;
        if (existing.isPresent()) {
            cartEntity = existing.get();
        } else {
            cartEntity = CartMapper.toEntity(cart);
        }
        final CartEntity savedCart = cartJpaRepository.save(cartEntity);

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
