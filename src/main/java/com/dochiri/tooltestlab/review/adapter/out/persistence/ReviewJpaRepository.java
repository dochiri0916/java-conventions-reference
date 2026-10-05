package com.dochiri.tooltestlab.review.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {
    Optional<ReviewEntity> findByPublicId(String publicId);

    List<ReviewEntity> findByProductIdOrderByIdDesc(String productId);

    boolean existsByMemberIdAndProductId(String memberId, String productId);

    void deleteByPublicId(String publicId);
}
