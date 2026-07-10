package com.dochiri.tooltestlab.global.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApiErrorContractValidatorTest {

    @Test
    @DisplayName("API 오류 매핑과 메시지 코드가 다르면 검증에 실패한다")
    void rejectMismatchedCodes() {
        // given
        final ErrorCodeMappingProvider mappingProvider = () -> Map.of(
                "MAPPING.CODE", mapping());
        final ApiErrorMessageProvider messageProvider = () -> Map.of(
                "MESSAGE.CODE", new ApiErrorMessage("오류", "오류가 발생했습니다."));
        final ApiErrorContractValidator validator = new ApiErrorContractValidator(
                List.of(mappingProvider),
                List.of(messageProvider));

        // when & then
        assertThatThrownBy(validator::validate)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("일치하지 않습니다");
    }

    @Test
    @DisplayName("API 오류 코드가 중복되면 검증에 실패한다")
    void rejectDuplicateCodes() {
        // given
        final ErrorCodeMappingProvider mappingProvider = () -> Map.of(
                "DUPLICATE.CODE", mapping());
        final ApiErrorMessageProvider emptyMessageProvider = Map::of;
        final ApiErrorContractValidator validator = new ApiErrorContractValidator(
                List.of(mappingProvider, mappingProvider),
                List.of(emptyMessageProvider));

        // when & then
        assertThatThrownBy(validator::validate)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("중복 코드");
    }

    private static ApiExceptionMapper.Mapping mapping() {
        return new ApiExceptionMapper.Mapping(HttpStatus.BAD_REQUEST, ProblemType.VALIDATION_FAILED);
    }
}
