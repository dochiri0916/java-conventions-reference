package com.dochiri.tooltestlab.category.application.exception;

import com.dochiri.tooltestlab.catalog.category.application.exception.CategoryErrorCode;
import com.dochiri.tooltestlab.catalog.category.application.exception.DuplicateCategoryNameException;
import com.dochiri.tooltestlab.catalog.category.application.exception.InvalidCommandException;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryAppExceptionTest {

    @Test
    @DisplayName("CategoryErrorCode 모든 코드가 안정적인 오류 계약을 가진다")
    void errorCodes() {
        // given
        var codes = CategoryErrorCode.values();

        // when & then
        for (var code : codes) {
            assertThat(code.code()).isNotBlank();
            assertThat(code.kind()).isNotNull();
            assertThat(code.detail()).isNotBlank();
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
        assertThat(ex.errorCode()).isEqualTo(expectedCode);
        assertThat(ex.getMessage()).isEqualTo(expectedDetail);
    }

    @Test
    @DisplayName("중복 카테고리명 예외는 안전한 이름을 extension으로 보관한다")
    void duplicateCategoryName() {
        // given
        final CategoryName categoryName = new CategoryName("전자기기");

        // when
        final DuplicateCategoryNameException exception =
                DuplicateCategoryNameException.duplicateCategoryName(categoryName);

        // then
        assertThat(exception.errorCode()).isEqualTo(CategoryErrorCode.DUPLICATE_CATEGORY_NAME);
        assertThat(exception.extensions()).containsEntry("categoryName", "전자기기");
    }
}
