package com.dochiri.tooltestlab.catalog.category.application.port.out;

import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryName;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;

import java.util.Set;

/**
 * 기술적 저장소 작업 실패는 {@link RepositoryOperationException}으로 전달한다.
 */
public interface CategoryRepositoryPort {

    Set<Category> findAllByIds(Set<CategoryId> categoryIds);

    Set<Category> findAllByNames(Set<CategoryName> categoryNames);

    Set<Category> findAll();

    Category create(Category category);

    Set<Category> createAll(Set<Category> categories);

}
