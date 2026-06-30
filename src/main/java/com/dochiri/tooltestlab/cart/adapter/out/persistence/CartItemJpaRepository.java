package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {
    List<CartItemEntity> findByCartId(String cartId);

    void deleteByCartId(String cartId);
}
