package com.dochiri.tooltestlab.cart.application.port.out;

import com.dochiri.tooltestlab.cart.application.exception.CartCreationConflictException;
import com.dochiri.tooltestlab.cart.domain.Cart;
import com.dochiri.tooltestlab.cart.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;

import java.util.Optional;

/**
 * 장바구니 저장소 Port (Application 소유).
 * <p>
 * 같은 회원의 장바구니 생성 충돌은 {@link CartCreationConflictException}으로 전달한다.
 * 기술적 저장소 작업 실패는 {@link RepositoryOperationException}으로 전달한다.
 */
public interface CartRepositoryPort {
    Cart save(Cart cart);

    Optional<Cart> findByMemberId(MemberReferenceId memberId);

    Optional<Cart> findByMemberIdForUpdate(MemberReferenceId memberId);

    Carts findPage(int page, int size);
}
