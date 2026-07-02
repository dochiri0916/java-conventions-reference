package com.dochiri.tooltestlab.member.adapter.in.web;

import com.dochiri.tooltestlab.global.error.ApiExceptionMapper;
import com.dochiri.tooltestlab.global.error.ProblemType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MemberErrorCodeMappingProviderTest {

    @Test
    @DisplayName("회원 에러 코드 16개를 모두 매핑한다")
    void hasAllMappings() {
        // given
        var provider = new MemberErrorCodeMappingProvider();

        // when
        Map<String, ApiExceptionMapper.Mapping> mappings = provider.errorCodeMappings();

        // then
        assertThat(mappings).isNotNull().hasSize(16);
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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("MEMBER_NOT_FOUND");

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("LOGIN_FAILED");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.UNAUTHORIZED);
    }

    @Test
    @DisplayName("AUTHENTICATION_REQUIRED는 401 UNAUTHORIZED로 매핑한다")
    void authenticationRequired() {
        // given
        var provider = new MemberErrorCodeMappingProvider();

        // when
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("AUTHENTICATION_REQUIRED");

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("DUPLICATE_EMAIL");

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("MEMBER_ID_REQUIRED");

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
        ApiExceptionMapper.Mapping mapping = provider.errorCodeMappings().get("INVALID_INPUT");

        // then
        assertThat(mapping).isNotNull();
        assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }
}
