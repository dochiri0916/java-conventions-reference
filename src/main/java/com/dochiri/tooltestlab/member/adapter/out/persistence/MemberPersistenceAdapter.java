package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.Member;
import com.dochiri.tooltestlab.member.domain.vo.Email;
import com.dochiri.tooltestlab.member.domain.vo.MemberId;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;

@Repository
@RequiredArgsConstructor
class MemberPersistenceAdapter implements MemberRepositoryPort {

    private static final Pattern EMAIL_CONSTRAINT = Pattern.compile(
            Pattern.quote("uk_members_email") + "(?:_INDEX_[0-9A-F]+)?",
            Pattern.CASE_INSENSITIVE);

    private final MemberJpaRepository jpaRepository;

    @Override
    public Member create(final Member member) {
        return execute(() -> {
            try {
                final MemberEntity entity = MemberMapper.toEntity(member);
                final MemberEntity saved = jpaRepository.saveAndFlush(entity);
                return MemberMapper.toDomain(saved);
            } catch (final DataIntegrityViolationException exception) {
                if (isEmailConstraintViolation(exception)) {
                    throw new DuplicateEmailException();
                }
                throw exception;
            }
        });
    }

    private static boolean isEmailConstraintViolation(final Throwable exception) {
        for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
            if (cause instanceof ConstraintViolationException violation) {
                final String constraintName = violation.getConstraintName();
                if (constraintName == null) {
                    return false;
                }
                // H2는 스키마와 자동 생성한 유니크 인덱스 접미사를 함께 반환한다.
                final String unquoted = constraintName.replace("\"", "").replace("`", "");
                final String name = unquoted.substring(unquoted.lastIndexOf('.') + 1);
                return EMAIL_CONSTRAINT.matcher(name).matches();
            }
        }
        return false;
    }

    @Override
    public Optional<Member> findByMemberId(final MemberId memberId) {
        return execute(() -> jpaRepository.findByPublicId(memberId.value()).map(MemberMapper::toDomain));
    }

    @Override
    public Optional<Member> findByEmail(final Email email) {
        return execute(() -> jpaRepository.findByEmail(email.value()).map(MemberMapper::toDomain));
    }

    @Override
    public boolean existsByEmail(final Email email) {
        return execute(() -> jpaRepository.existsByEmail(email.value()));
    }
}
