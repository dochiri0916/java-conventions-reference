package com.dochiri.tooltestlab.cart.application.port.out;

import com.dochiri.tooltestlab.cart.domain.model.Cart;

import java.util.Optional;

/**
 * 장바구니 저장소 Port (Application 소유).
 */
public interface CartRepositoryPort {
    Cart save(Cart cart);

    Optional<Cart> findByMemberId(String memberPublicId);
}
