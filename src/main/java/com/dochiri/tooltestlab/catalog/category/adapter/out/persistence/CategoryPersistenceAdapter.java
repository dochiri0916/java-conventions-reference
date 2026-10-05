package com.dochiri.tooltestlab.catalog.category.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.application.exception.DuplicateCategoryNameException;
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryName;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;

@Repository
@RequiredArgsConstructor
class CategoryPersistenceAdapter implements CategoryRepositoryPort {

    private static final Pattern NAME_CONSTRAINT = Pattern.compile(
            Pattern.quote("uk_categories_name") + "(?:_INDEX_[0-9A-F]+)?",
            Pattern.CASE_INSENSITIVE);

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public Set<Category> findAllByIds(final Set<CategoryId> categoryIds) {
        return execute(() -> {
            if (categoryIds.isEmpty()) {
                return Set.of();
            }

            return categoryJpaRepository.findAllByPublicIdIn(
                            categoryIds.stream().map(CategoryId::value).toList()
                    ).stream()
                    .map(CategoryMapper::toDomain)
                    .collect(Collectors.toUnmodifiableSet());
        });
    }

    @Override
    public Set<Category> findAllByNames(final Set<CategoryName> categoryNames) {
        return execute(() -> {
            if (categoryNames.isEmpty()) {
                return Set.of();
            }

            return categoryJpaRepository.findAllByNameIn(
                            categoryNames.stream().map(CategoryName::value).toList()
                    ).stream()
                    .map(CategoryMapper::toDomain)
                    .collect(Collectors.toUnmodifiableSet());
        });
    }

    @Override
    public Set<Category> findAll() {
        return execute(() -> categoryJpaRepository.findAll().stream()
                .map(CategoryMapper::toDomain)
                .collect(Collectors.toUnmodifiableSet()));
    }

    @Override
    public Category create(final Category category) {
        return execute(() -> {
            try {
                final CategoryEntity saved = categoryJpaRepository.saveAndFlush(CategoryMapper.toEntity(category));
                return CategoryMapper.toDomain(saved);
            } catch (final DataIntegrityViolationException exception) {
                if (isNameConstraintViolation(exception)) {
                    throw new DuplicateCategoryNameException();
                }
                throw exception;
            }
        });
    }

    @Override
    public Set<Category> createAll(final Set<Category> categories) {
        return execute(() -> {
            if (categories.isEmpty()) {
                return Set.of();
            }

            try {
                return categoryJpaRepository.saveAllAndFlush(
                                categories.stream().map(CategoryMapper::toEntity).toList()
                        ).stream()
                        .map(CategoryMapper::toDomain)
                        .collect(Collectors.toUnmodifiableSet());
            } catch (final DataIntegrityViolationException exception) {
                if (isNameConstraintViolation(exception)) {
                    throw new DuplicateCategoryNameException();
                }
                throw exception;
            }
        });
    }

    private static boolean isNameConstraintViolation(final Throwable exception) {
        for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
            if (cause instanceof ConstraintViolationException violation) {
                final String constraintName = violation.getConstraintName();
                if (constraintName == null) {
                    return false;
                }
                // H2는 스키마와 자동 생성한 유니크 인덱스 접미사를 함께 반환한다.
                final String unquoted = constraintName.replace("\"", "").replace("`", "");
                final String name = unquoted.substring(unquoted.lastIndexOf('.') + 1);
                return NAME_CONSTRAINT.matcher(name).matches();
            }
        }
        return false;
    }
}
