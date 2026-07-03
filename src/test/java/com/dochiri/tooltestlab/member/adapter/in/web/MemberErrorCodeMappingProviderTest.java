package com.dochiri.tooltestlab.member.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ApiErrorCode;
import com.dochiri.tooltestlab.global.error.ProblemType;
import com.dochiri.tooltestlab.member.application.exception.DuplicateEmailException;
import com.dochiri.tooltestlab.member.application.exception.MemberErrorCode;
import com.dochiri.tooltestlab.member.domain.exception.MemberDomainErrorCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import com.dochiri.tooltestlab.member.application.exception.InvalidCommandException;
import com.dochiri.tooltestlab.member.application.exception.LoginFailedException;
import com.dochiri.tooltestlab.member.application.exception.MemberNotFoundException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidEmailException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberIdException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidMemberNameException;
import com.dochiri.tooltestlab.member.domain.exception.InvalidPasswordException;

class MemberErrorCodeMappingProviderTest {

    @Test
    @DisplayName("회원 에러 코드 15개를 모두 매핑한다")
    void hasAllMappings() {
        // given
        var provider = new MemberErrorCodeMappingProvider();

        // when
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // then
        assertThat(mappings).isNotNull().hasSize(15);
    }

    @Test
    @DisplayName("모든 매핑이 non-null status와 problemType을 가진다")
    void allMappingsHaveValidStatusAndProblemType() {
        // given
        var provider = new MemberErrorCodeMappingProvider();
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // when & then
        assertThat(mappings.values()).allSatisfy(mapping -> {
            assertThat(mapping.status()).isNotNull();
            assertThat(mapping.problemType()).isNotNull();
        });
    }

    @Test
    @DisplayName("MEMBER_NOT_FOUND는 404 NOT_FOUND로 매핑한다")
    void memberNotFound() {
        // given
        var provider = new MemberErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(MemberErrorCode.MEMBER_NOT_FOUND));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.NOT_FOUND);
    }

    @Test
    @DisplayName("LOGIN_FAILED는 401 UNAUTHORIZED로 매핑한다")
    void loginFailed() {
        // given
        var provider = new MemberErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(MemberErrorCode.LOGIN_FAILED));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.UNAUTHORIZED);
    }

    @Test
    @DisplayName("DUPLICATE_EMAIL은 409 CONFLICT로 매핑한다")
    void duplicateEmail() {
        // given
        var provider = new MemberErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(MemberErrorCode.DUPLICATE_EMAIL));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.CONFLICT);
    }

    @Test
    @DisplayName("MEMBER_ID_REQUIRED는 400 VALIDATION_FAILED로 매핑한다")
    void memberIdRequired() {
        // given
        var provider = new MemberErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(MemberDomainErrorCode.MEMBER_ID_REQUIRED));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("INVALID_INPUT은 400 VALIDATION_FAILED로 매핑한다")
    void invalidInput() {
        // given
        var provider = new MemberErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings()
                .get(ApiErrorCode.from(MemberErrorCode.INVALID_INPUT));

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("회원 예외 객체를 타입 기반으로 매핑한다")
    void resolveMemberExceptionByType() {
        // given
        var provider = new MemberErrorCodeMappingProvider();
        var exception = DuplicateEmailException.duplicateEmail("test@test.com");

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(MemberErrorCode.DUPLICATE_EMAIL));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("InvalidMemberIdException을 타입 기반으로 매핑한다")
    void resolveInvalidMemberIdException() {
        // given
        var provider = new MemberErrorCodeMappingProvider();
        var exception = InvalidMemberIdException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(MemberDomainErrorCode.MEMBER_ID_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidEmailException을 타입 기반으로 매핑한다")
    void resolveInvalidEmailException() {
        // given
        var provider = new MemberErrorCodeMappingProvider();
        var exception = InvalidEmailException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(MemberDomainErrorCode.EMAIL_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidPasswordException을 타입 기반으로 매핑한다")
    void resolveInvalidPasswordException() {
        // given
        var provider = new MemberErrorCodeMappingProvider();
        var exception = InvalidPasswordException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(MemberDomainErrorCode.PASSWORD_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidMemberNameException을 타입 기반으로 매핑한다")
    void resolveInvalidMemberNameException() {
        // given
        var provider = new MemberErrorCodeMappingProvider();
        var exception = InvalidMemberNameException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(MemberDomainErrorCode.MEMBER_NAME_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("InvalidMemberException을 타입 기반으로 매핑한다")
    void resolveInvalidMemberException() {
        // given
        var provider = new MemberErrorCodeMappingProvider();
        var exception = InvalidMemberException.required();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code())
                .isEqualTo(ApiErrorCode.from(MemberDomainErrorCode.MEMBER_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("MemberNotFoundException을 타입 기반으로 매핑한다")
    void resolveMemberNotFoundException() {
        // given
        var provider = new MemberErrorCodeMappingProvider();
        var exception = MemberNotFoundException.memberNotFound("member1");

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(MemberErrorCode.MEMBER_NOT_FOUND));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("LoginFailedException을 타입 기반으로 매핑한다")
    void resolveLoginFailedException() {
        // given
        var provider = new MemberErrorCodeMappingProvider();
        var exception = LoginFailedException.loginFailed();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(MemberErrorCode.LOGIN_FAILED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("회원 InvalidCommandException을 타입 기반으로 매핑한다")
    void resolveInvalidCommandException() {
        // given
        var provider = new MemberErrorCodeMappingProvider();
        var exception = InvalidCommandException.nameRequired();

        // when
        var mappedError = provider.resolve(exception);

        // then
        assertThat(mappedError).isPresent();
        assertThat(mappedError.orElseThrow().code()).isEqualTo(ApiErrorCode.from(MemberErrorCode.NAME_REQUIRED));
        assertThat(mappedError.orElseThrow().mapping().status()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
