package com.dochiri.tooltestlab.category.application.exception;

import com.dochiri.tooltestlab.catalog.category.application.exception.CategoryErrorCode;
import com.dochiri.tooltestlab.catalog.category.application.exception.InvalidCommandException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryAppExceptionTest {

    @Test
    @DisplayName("CategoryErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = CategoryErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.name()).isNotBlank();
        }
    }

    @Test
    @DisplayName("InvalidCommandException 정상 생성")
    void invalidCommand() {
        // given
        var expectedCode = CategoryErrorCode.INVALID_INPUT;
        var expectedDetail = "detail";

        // when
        var ex = InvalidCommandException.invalidInput(expectedDetail);

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
        assertThat(ex.detail()).isEqualTo(expectedDetail);
    }
}
