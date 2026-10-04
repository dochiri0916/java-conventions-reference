package com.dochiri.tooltestlab.cart.application.port.out;

import com.dochiri.tooltestlab.cart.domain.Cart;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;

import java.util.Optional;
import java.util.List;

/**
 * 장바구니 저장소 Port (Application 소유).
 * <p>
 * 기술적 저장소 작업 실패는 {@link RepositoryOperationException}으로 전달한다.
 */
public interface CartRepositoryPort {
    Cart save(Cart cart);

    Optional<Cart> findByMemberId(String memberPublicId);

    Optional<Cart> findByMemberIdForUpdate(String memberPublicId);

    List<Cart> findPage(int page, int size);
}
