package com.dochiri.tooltestlab.cart.adapter.out.persistence;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface CartJpaRepository extends JpaRepository<CartEntity, Long> {
    Optional<CartEntity> findByMemberId(String memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CartEntity c WHERE c.memberId = :memberId")
    Optional<CartEntity> findByMemberIdForUpdate(@Param("memberId") String memberId);
}
