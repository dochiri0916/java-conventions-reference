package com.dochiri.tooltestlab.member.adapter.out.persistence;

import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.exception.MemberErrorCode;
import com.dochiri.tooltestlab.member.domain.model.Email;
import com.dochiri.tooltestlab.member.domain.model.EncodedPassword;
import com.dochiri.tooltestlab.member.domain.model.Member;
import com.dochiri.tooltestlab.member.domain.model.MemberName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberPersistenceAdapterTest {

    @Mock
    private MemberJpaRepository jpaRepository;

    private MemberPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new MemberPersistenceAdapter(jpaRepository);
    }

    @Test
    @DisplayName("회원 이메일 제약 위반은 중복 이메일 예외로 번역한다")
    void translatesDuplicateEmailConstraintViolation() {
        // given
        final Member member = Member.register(
                new Email("duplicate@example.com"),
                new EncodedPassword("encoded-password"),
                new MemberName("홍길동"));
        when(jpaRepository.saveAndFlush(any(MemberEntity.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate email"));

        // when & then
        assertThatThrownBy(() -> adapter.save(member))
                .isInstanceOf(DuplicateEmailException.class)
                .extracting(exception -> ((DuplicateEmailException) exception).code())
                .isEqualTo(MemberErrorCode.DUPLICATE_EMAIL);
    }
}
