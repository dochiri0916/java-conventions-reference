package com.dochiri.tooltestlab.member.adapter.out.persistence;

import org.junit.jupiter.api.DisplayName;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;
import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.port.out.MemberRepositoryPort;
import com.dochiri.tooltestlab.member.domain.Member;
import com.dochiri.tooltestlab.member.domain.vo.Email;
import com.dochiri.tooltestlab.member.domain.vo.EncodedPassword;
import com.dochiri.tooltestlab.member.domain.vo.MemberName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DataJpaTest(showSql = false)
@Import({MemberPersistenceAdapter.class, MemberPersistenceAdapterTest.AuditingConfig.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class MemberPersistenceAdapterTest {

    @Autowired MemberRepositoryPort members;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired JdbcTemplate jdbc;

    @Test
    @DisplayName("회원 ID를 publicId 컬럼으로 저장하고 조회한다")
    void stores_and_loads_member_id_through_public_id_column() {
        // given
        final Member member = create(member(uniqueEmail()));
        final String publicId = jdbc.queryForObject(
                "select public_id from members where email = ?", String.class, member.email().value());
        final Member loaded = members.findByMemberId(member.id()).orElseThrow();

        // when & then
        assertEquals(member.id().value(), publicId);
        assertEquals(member.id(), loaded.id());
        assertEquals(member.email(), loaded.email());
    }

    @Test
    @DisplayName("애플리케이션 사전 확인 없이도 중복 이메일을 거부한다")
    void rejects_duplicate_email_without_application_precheck() {
        // given
        final String email = uniqueEmail();
        create(member(email));

        // when & then
        assertThrows(DuplicateEmailException.class, () -> create(member(email)));
        assertEquals(1L, count(email));
    }

    @Test
    @DisplayName("중복 회원 ID는 저장소 예외로 번역한다")
    void translates_duplicate_member_id_as_repository_failure() {
        // given
        final Member original = create(member(uniqueEmail()));
        final Member duplicateId = Member.restore(original.id(), Email.of(uniqueEmail()),
                original.encodedPassword(), original.name(), original.status());

        // when & then
        final RepositoryOperationException exception = assertThrows(
                RepositoryOperationException.class, () -> create(duplicateId));
        assertInstanceOf(org.springframework.dao.DataIntegrityViolationException.class, exception.getCause());
    }

    @Test
    @DisplayName("컬럼 길이 위반은 저장소 예외로 번역한다")
    void translates_column_length_violation_as_repository_failure() {
        // given
        final Member invalid = Member.create(Email.of(uniqueEmail()),
                EncodedPassword.of("encoded-password"), MemberName.of("a".repeat(256)));

        // when & then
        assertThrows(RepositoryOperationException.class, () -> create(invalid));
    }

    @Test
    @DisplayName("같은 이메일의 동시 저장은 하나만 허용한다")
    void allows_only_one_concurrent_insert_for_the_same_email() throws Exception {
        // given
        final String email = uniqueEmail();
        final CyclicBarrier barrier = new CyclicBarrier(2);

        // when & then
        try (final var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            final var first = executor.submit(() -> createConcurrently(member(email), barrier));
            final var second = executor.submit(() -> createConcurrently(member(email), barrier));

            final boolean firstCreated = first.get(10, TimeUnit.SECONDS);
            final boolean secondCreated = second.get(10, TimeUnit.SECONDS);
            assertNotEquals(firstCreated, secondCreated);
        }
        assertEquals(1L, count(email));
    }

    private boolean createConcurrently(final Member member, final CyclicBarrier barrier) throws Exception {
        barrier.await(5, TimeUnit.SECONDS);
        try {
            create(member);
            return true;
        } catch (final DuplicateEmailException exception) {
            return false;
        }
    }

    private Member create(final Member member) {
        return new TransactionTemplate(transactionManager).execute(status -> members.create(member));
    }

    private long count(final String email) {
        return jdbc.queryForObject("select count(*) from members where email = ?", Long.class, email);
    }

    private static Member member(final String email) {
        return Member.create(Email.of(email), EncodedPassword.of("encoded-password"), MemberName.of("회원"));
    }

    private static String uniqueEmail() {
        return UUID.randomUUID() + "@example.com";
    }

    @TestConfiguration
    @EnableJpaAuditing
    static class AuditingConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("test");
        }
    }
}
