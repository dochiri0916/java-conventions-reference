package com.dochiri.tooltestlab.category.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryExceptionTest {

    @Test
    @DisplayName("InvalidCategoryIdException 정상 생성")
    void invalidCatId() {
        // given
        var expectedCode = CategoryDomainErrorCode.INVALID_CAT_ID;

        // when
        var ex = InvalidCategoryIdException.invalidCatId();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("InvalidCategoryNameException 정상 생성")
    void invalidCatName() {
        // given
        var expectedCode = CategoryDomainErrorCode.INVALID_CAT_NAME;

        // when
        var ex = InvalidCategoryNameException.invalidCatName();

        // then
        assertThat(ex.code()).isEqualTo(expectedCode);
    }

    @Test
    @DisplayName("CategoryDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
        // given
        var codes = CategoryDomainErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
