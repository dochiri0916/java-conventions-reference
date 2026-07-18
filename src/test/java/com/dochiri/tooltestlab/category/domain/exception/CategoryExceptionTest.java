package com.dochiri.tooltestlab.category.domain.exception;

import com.dochiri.tooltestlab.catalog.category.domain.exception.CategoryDomainErrorCode;
import com.dochiri.tooltestlab.catalog.category.domain.exception.InvalidCategoryIdException;
import com.dochiri.tooltestlab.catalog.category.domain.exception.InvalidCategoryNameException;
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
    @DisplayName("InvalidCategoryIdException required 정상 생성")
    void categoryIdRequired() {
        // given
        var expectedCode = CategoryDomainErrorCode.CATEGORY_ID_REQUIRED;

        // when
        var ex = InvalidCategoryIdException.required();

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
    @DisplayName("InvalidCategoryNameException required 정상 생성")
    void categoryNameRequired() {
        // given
        var expectedCode = CategoryDomainErrorCode.CATEGORY_NAME_REQUIRED;

        // when
        var ex = InvalidCategoryNameException.required();

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
            assertThat(code.name()).isNotBlank();
        }
    }
}
