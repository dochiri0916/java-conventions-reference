package com.dochiri.tooltestlab.catalog.category.application.port.out;

import com.dochiri.tooltestlab.catalog.category.application.exception.DuplicateCategoryNameException;
import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;

/**
 * 카테고리 이름 중복은 {@link DuplicateCategoryNameException}으로 전달한다.
 * 기술적 저장소 작업 실패는 {@link RepositoryOperationException}으로 전달한다.
 */
public interface CategoryRepositoryPort {

    Categories findAllByIds(CategoryIdSet categoryIds);

    Categories findAllByNames(CategoryNameSet categoryNames);

    Categories findAll();

    Category create(Category category);

    Categories createAll(Categories categories);

}
