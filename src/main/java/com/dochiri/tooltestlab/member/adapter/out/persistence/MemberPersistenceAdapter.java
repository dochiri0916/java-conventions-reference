package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class MemberPersistenceAdapter implements MemberRepositoryPort {

    private final MemberJpaRepository jpaRepository;

    @Override
    public Member save(final Member member) {
        try {
            final MemberEntity entity = MemberMapper.toEntity(member);
            final MemberEntity saved = jpaRepository.saveAndFlush(entity);
            return MemberMapper.toDomain(saved);
        } catch (DataIntegrityViolationException exception) {
            throw DuplicateEmailException.duplicateEmail(member.email().value());
        }
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
