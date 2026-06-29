package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.model.Member;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "tool-test-lab.concurrency.safe", havingValue = "false", matchIfMissing = true)
class MemberPersistenceAdapter implements MemberRepositoryPort {

    private final MemberJpaRepository jpaRepository;

    MemberPersistenceAdapter(final MemberJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Member save(final Member member) {
        final MemberEntity entity = MemberMapper.toEntity(member);
        final MemberEntity saved = jpaRepository.save(entity);
        return MemberMapper.toDomain(saved);
    }

    @Override
    public Optional<Member> findByPublicId(final String publicId) {
        return jpaRepository.findByPublicId(publicId).map(MemberMapper::toDomain);
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
