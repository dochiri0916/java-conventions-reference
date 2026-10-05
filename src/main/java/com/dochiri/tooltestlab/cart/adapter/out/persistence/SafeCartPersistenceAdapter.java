package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import com.dochiri.tooltestlab.cart.application.exception.CartCreationConflictException;
import com.dochiri.tooltestlab.cart.application.port.out.CartRepositoryPort;
import com.dochiri.tooltestlab.cart.domain.Cart;
import com.dochiri.tooltestlab.cart.domain.vo.MemberReferenceId;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;

/**
 * 동시성 안전 모드용 CartPersistenceAdapter.
 * {@code tool-test-lab.concurrency.safe=true}일 때 활성화.
 * PESSIMISTIC_WRITE 락으로 동시 수정을 직렬화한다.
 * <p>
 * PESSIMISTIC_WRITE 락은 기존 row에만 적용되므로, 최초 생성 시에는
 * 회원별 장바구니 UNIQUE 제약 위반이 발생할 수 있다.
 * 실패한 트랜잭션에서는 재시도하지 않고 생성 충돌 예외를 전달한다.
 */
@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "true")
class SafeCartPersistenceAdapter implements CartRepositoryPort {

    private final CartJpaRepository cartJpaRepository;
    private final CartItemJpaRepository cartItemJpaRepository;
    private final EntityManager entityManager;

    @Override
    public Cart save(final Cart cart) {
        return execute(() -> {
            try {
                return doSave(cart);
            } catch (final DataIntegrityViolationException exception) {
                if (CartConstraintViolationClassifier.isMemberIdViolation(exception)) {
                    // 제약 위반으로 실패한 트랜잭션에서는 다시 조회하거나 저장하지 않는다.
                    throw new CartCreationConflictException(exception);
                }
                throw exception;
            }
        });
    }

    private Cart doSave(final Cart cart) {
        final Optional<CartEntity> existing = cartJpaRepository.findByMemberIdForUpdate(cart.memberId().value());
        if (existing.isPresent()) {
            final CartEntity cartEntity = existing.get();
            cartEntity.updateRevision(cart.revision());
            final CartEntity savedCart = cartJpaRepository.saveAndFlush(cartEntity);
            cartItemJpaRepository.deleteByCartId(savedCart.getPublicId());
            cartItemJpaRepository.flush();
            final List<CartItemEntity> itemEntities = CartMapper.toItemEntities(cart, savedCart.getPublicId());
            final List<CartItemEntity> savedItems = cartItemJpaRepository.saveAllAndFlush(itemEntities);
            return CartMapper.toDomain(savedCart, savedItems);
        }

        final CartEntity cartEntity = CartMapper.toEntity(cart);
        final CartEntity savedCart = cartJpaRepository.saveAndFlush(cartEntity);
        cartItemJpaRepository.deleteByCartId(savedCart.getPublicId());
        cartItemJpaRepository.flush();
        final List<CartItemEntity> itemEntities = CartMapper.toItemEntities(cart, savedCart.getPublicId());
        final List<CartItemEntity> savedItems = cartItemJpaRepository.saveAllAndFlush(itemEntities);
        return CartMapper.toDomain(savedCart, savedItems);
    }

    @Override
    public List<Cart> findPage(final int page, final int size) {
        return execute(() -> cartJpaRepository.findAll(PageRequest.of(page, size)).getContent().stream()
                .map(entity -> CartMapper.toDomain(entity, cartItemJpaRepository.findByCartId(entity.getPublicId())))
                .toList());
    }

    @Override
    public Optional<Cart> findByMemberIdForUpdate(final MemberReferenceId memberId) {
        return execute(() -> cartJpaRepository.findByMemberIdForUpdate(memberId.value()).map(cartEntity -> {
            entityManager.refresh(cartEntity);
            return CartMapper.toDomain(cartEntity, cartItemJpaRepository.findByCartId(cartEntity.getPublicId()));
        }));
    }

    @Override
    public Optional<Cart> findByMemberId(final MemberReferenceId memberId) {
        return execute(() -> cartJpaRepository.findByMemberId(memberId.value()).flatMap(cartEntity -> {
            final List<CartItemEntity> items = cartItemJpaRepository.findByCartId(cartEntity.getPublicId());
            return Optional.of(CartMapper.toDomain(cartEntity, items));
        }));
    }
}
