package com.dochiri.tooltestlab.member.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByMemberId(String memberId);

    Optional<MemberEntity> findByEmail(String email);

    boolean existsByEmail(String email);

}
