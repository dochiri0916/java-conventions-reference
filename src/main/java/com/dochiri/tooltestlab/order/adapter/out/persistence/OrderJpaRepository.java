package com.dochiri.tooltestlab.order.adapter.out.persistence;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByPublicId(String publicId);

    List<OrderEntity> findByMemberPublicIdOrderByIdDesc(String memberPublicId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM OrderEntity o WHERE o.publicId = :publicId")
    Optional<OrderEntity> findByPublicIdForUpdate(@Param("publicId") String publicId);
}
