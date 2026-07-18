package com.dochiri.tooltestlab.global.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

class ApiExceptionMapperTest {

    @Nested
    @DisplayName("Provider 조합 단위 테스트")
    class Composition {

        @Test
        @DisplayName("여러 provider의 매핑을 조합하여 resolve한다")
        void composesMultipleProviders() {
            // given
            var providerA = (ErrorCodeMappingProvider) () -> Map.of(
                    "CODE_A", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED));
            var providerB = (ErrorCodeMappingProvider) () -> Map.of(
                    "CODE_B", new ApiExceptionMapper.Mapping(HttpStatus.NOT_FOUND, ProblemType.NOT_FOUND));
            var mapper = new ApiExceptionMapper(List.of(providerA, providerB));

            // when
            ApiExceptionMapper.Mapping mappingA = mapper.map("CODE_A");
            ApiExceptionMapper.Mapping mappingB = mapper.map("CODE_B");

            // then
            assertThat(mappingA.status()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(mappingA.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
            assertThat(mappingB.status()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(mappingB.problemType()).isEqualTo(ProblemType.NOT_FOUND);
        }

        @Test
        @DisplayName("알 수 없는 에러 코드는 500 INTERNAL_ERROR를 반환한다")
        void unknownCodeReturnsInternalError() {
            // given
            var provider = (ErrorCodeMappingProvider) () -> Map.of(
                    "KNOWN", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED));
            var mapper = new ApiExceptionMapper(List.of(provider));

            // when
            ApiExceptionMapper.Mapping mapping = mapper.map("UNKNOWN_CODE");

            // then
            assertThat(mapping.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(mapping.problemType()).isEqualTo(ProblemType.INTERNAL_ERROR);
        }

        @Test
        @DisplayName("provider가 없어도 unknown code는 500을 반환한다")
        void emptyProvidersReturnsDefaultForUnknownCode() {
            // given
            var mapper = new ApiExceptionMapper(List.of());

            // when
            ApiExceptionMapper.Mapping mapping = mapper.map("ANY_CODE");

            // then
            assertThat(mapping.status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(mapping.problemType()).isEqualTo(ProblemType.INTERNAL_ERROR);
        }

        @Test
        @DisplayName("중복 키는 첫 번째 provider의 매핑을 유지한다")
        void duplicateKeysKeepFirstMapping() {
            // given
            var providerA = (ErrorCodeMappingProvider) () -> Map.of(
                    "DUPE", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED));
            var providerB = (ErrorCodeMappingProvider) () -> Map.of(
                    "DUPE", new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED));
            var mapper = new ApiExceptionMapper(List.of(providerA, providerB));

            // when
            ApiExceptionMapper.Mapping mapping = mapper.map("DUPE");

            // then
            assertThat(mapping.status()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(mapping.problemType()).isEqualTo(ProblemType.VALIDATION_FAILED);
        }
    }
}
