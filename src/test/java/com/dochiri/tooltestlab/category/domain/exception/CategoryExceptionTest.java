package com.dochiri.tooltestlab.category.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryExceptionTest {

    @Test
    @DisplayName("InvalidCategoryIdException 정상 생성")
    void invalidCatId() {
            // given

            // when

            // then

        var ex = InvalidCategoryIdException.invalidCatId();
        assertThat(ex.code()).isEqualTo(CategoryDomainErrorCode.INVALID_CAT_ID);
    }

    @Test
    @DisplayName("InvalidCategoryNameException 정상 생성")
    void invalidCatName() {
            // given

            // when

            // then

        var ex = InvalidCategoryNameException.invalidCatName();
        assertThat(ex.code()).isEqualTo(CategoryDomainErrorCode.INVALID_CAT_NAME);
    }

    @Test
    @DisplayName("CategoryDomainErrorCode 모든 코드가 title을 가진다")
    void errorCodes() {
            // given

            // when

            // then

        for (var code : CategoryDomainErrorCode.values()) {
            assertThat(code.title()).isNotBlank();
        }
    }
}
