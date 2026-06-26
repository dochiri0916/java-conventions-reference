package com.dochiri.tooltestlab.review.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {
    Optional<ReviewEntity> findByPublicId(String publicId);

    List<ReviewEntity> findByProductPublicIdOrderByIdDesc(String productPublicId);

    boolean existsByMemberPublicIdAndProductPublicId(String memberPublicId, String productPublicId);

    void deleteByPublicId(String publicId);
}
