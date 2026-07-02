package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class MemberPersistenceAdapter implements MemberRepositoryPort {

    private final MemberJpaRepository jpaRepository;

    @Override
    public Member save(final Member member) {
        final MemberEntity entity = MemberMapper.toEntity(member);
        final MemberEntity saved = jpaRepository.save(entity);
        return MemberMapper.toDomain(saved);
    }

    @Override
    public Optional<Member> findByMemberId(final String memberId) {
        return jpaRepository.findByMemberId(memberId).map(MemberMapper::toDomain);
    }

    @Override
    public Optional<Member> findByEmail(final String email) {
        return jpaRepository.findByEmail(email).map(MemberMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return jpaRepository.existsByEmail(email);
    }
}
