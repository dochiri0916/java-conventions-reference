package com.dochiri.tooltestlab.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.adapter.out.persistence.CategoryJpaRepository;
import com.dochiri.tooltestlab.catalog.category.application.exception.CategoryErrorCode;
import com.dochiri.tooltestlab.catalog.category.application.exception.DuplicateCategoryNameException;
import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;
import com.dochiri.tooltestlab.catalog.product.adapter.out.persistence.ProductJpaRepository;
import com.dochiri.tooltestlab.catalog.product.adapter.out.persistence.ProductPersistenceAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductPersistenceAdapterTest {

    @Mock
    private ProductJpaRepository productJpaRepository;

    @Mock
    private CategoryJpaRepository categoryJpaRepository;

    private ProductPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ProductPersistenceAdapter(productJpaRepository, categoryJpaRepository);
    }

    @Test
    @DisplayName("카테고리명 제약 위반은 중복 카테고리명 예외로 번역한다")
    void translatesDuplicateCategoryNameConstraintViolation() {
        // given
        final Category category = Category.create(new CategoryName("전자기기"));
        when(categoryJpaRepository.saveAndFlush(any()))
                .thenThrow(new DataIntegrityViolationException("duplicate category name"));

        // when & then
        assertThatThrownBy(() -> adapter.saveCategory(category))
                .isInstanceOf(DuplicateCategoryNameException.class)
                .satisfies(exception -> {
                    final DuplicateCategoryNameException duplicate =
                            (DuplicateCategoryNameException) exception;
                    assertThat(duplicate.code()).isEqualTo(CategoryErrorCode.DUPLICATE_CATEGORY_NAME);
                    assertThat(duplicate.categoryName()).isEqualTo(category.name());
                });
    }
}
