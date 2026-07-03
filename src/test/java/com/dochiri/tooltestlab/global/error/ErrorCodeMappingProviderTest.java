package com.dochiri.tooltestlab.global.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ErrorCodeMappingProviderTest {

    @Test
    @DisplayName("기본 resolve(RuntimeException)은 null을 받으면 예외를 던진다")
    void defaultResolveRuntimeExceptionRejectsNull() {
        // given
        ErrorCodeMappingProvider provider = Map::of;

        // when & then
        assertThatThrownBy(() -> provider.resolve((RuntimeException) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("기본 resolve(RuntimeException)은 매핑 불가능한 예외에 대해 빈 Optional을 반환한다")
    void defaultResolveRuntimeExceptionReturnsEmptyForUnknown() {
        // given
        ErrorCodeMappingProvider provider = Map::of;

        // when
        Optional<ApiExceptionMapper.MappedError> result = provider.resolve(new RuntimeException("unknown"));

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("resolve(Enum)에 null을 전달하면 NullPointerException을 던진다")
    void resolveEnumRejectsNull() {
        // given
        ErrorCodeMappingProvider provider = Map::of;

        // when & then
        assertThatThrownBy(() -> provider.resolve((Enum<?>) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("resolve(Enum)은 매핑된 에러 코드를 찾아 반환한다")
    void resolveEnumReturnsMappedError() {
        // given
        var provider = new StubProvider();

        // when
        var result = provider.resolve(TestErrorCode.TEST_ERROR);

        // then
        assertThat(result).isPresent();
        assertThat(result.orElseThrow().code()).isEqualTo("GLOBAL.TEST_ERROR");
        assertThat(result.orElseThrow().mapping().status()).isEqualTo(org.springframework.http.HttpStatus.BAD_REQUEST);
        assertThat(result.orElseThrow().mapping().problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
    }

    @Test
    @DisplayName("resolve(Enum)은 매핑되지 않은 에러 코드에 대해 빈 Optional을 반환한다")
    void resolveEnumReturnsEmptyForUnmappedCode() {
        // given
        var provider = new StubProvider();

        // when
        var result = provider.resolve(TestErrorCode.UNMAPPED_ERROR);

        // then
        assertThat(result).isEmpty();
    }

    private enum TestErrorCode {
        TEST_ERROR,
        UNMAPPED_ERROR
    }

    private static class StubProvider implements ErrorCodeMappingProvider {
        @Override
        public Map<String, ApiExceptionMapper.Mapping> errorCodeMappings() {
            return Map.of(
                    ApiErrorCode.from(TestErrorCode.TEST_ERROR),
                    new ApiExceptionMapper.Mapping(
                            org.springframework.http.HttpStatus.BAD_REQUEST,
                            ProblemType.VALIDATION_FAILED
                    )
            );
        }
    }
}
