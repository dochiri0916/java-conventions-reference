package com.dochiri.tooltestlab.category.application.service;

import com.dochiri.tooltestlab.catalog.category.application.exception.CategoryErrorCode;
import com.dochiri.tooltestlab.catalog.category.application.exception.DuplicateCategoryNameException;
import com.dochiri.tooltestlab.catalog.category.application.port.in.command.RegisterCategoryCommand;
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.application.service.RegisterCategoryService;
import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class RegisterCategoryServiceTest {

    @Test
    @DisplayName("새 카테고리를 등록한다")
    void register() {
        // given
        final CategoryRepositoryPort categoryRepositoryPort = mock(CategoryRepositoryPort.class);
        final RegisterCategoryService service = new RegisterCategoryService(categoryRepositoryPort);
        final RegisterCategoryCommand command = new RegisterCategoryCommand("전자기기");
        final Category category = Category.create("전자기기");
        given(categoryRepositoryPort.findAllByNames(Set.of(new CategoryName("전자기기"))))
                .willReturn(Set.of());
        given(categoryRepositoryPort.create(any(Category.class))).willReturn(category);

        // when
        final var result = service.register(command);

        // then
        assertThat(result.publicId()).isEqualTo(category.id().value());
        assertThat(result.name()).isEqualTo("전자기기");
        then(categoryRepositoryPort).should().create(any(Category.class));
    }

    @Test
    @DisplayName("이미 존재하는 카테고리명은 충돌 오류로 처리한다")
    void registerDuplicateName() {
        // given
        final CategoryRepositoryPort categoryRepositoryPort = mock(CategoryRepositoryPort.class);
        final RegisterCategoryService service = new RegisterCategoryService(categoryRepositoryPort);
        final RegisterCategoryCommand command = new RegisterCategoryCommand("전자기기");
        given(categoryRepositoryPort.findAllByNames(Set.of(new CategoryName("전자기기"))))
                .willReturn(Set.of(Category.create("전자기기")));

        // when & then
        assertThatThrownBy(() -> service.register(command))
                .isInstanceOfSatisfying(DuplicateCategoryNameException.class, exception -> {
                    assertThat(exception.errorCode())
                            .isEqualTo(CategoryErrorCode.DUPLICATE_CATEGORY_NAME);
                    assertThat(exception.extensions()).containsEntry("categoryName", "전자기기");
                });
        then(categoryRepositoryPort).should().findAllByNames(Set.of(new CategoryName("전자기기")));
        then(categoryRepositoryPort).shouldHaveNoMoreInteractions();
    }
}
