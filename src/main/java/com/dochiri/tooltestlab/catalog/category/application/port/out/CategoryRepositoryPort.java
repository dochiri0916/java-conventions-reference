package com.dochiri.tooltestlab.catalog.category.application.port.out;

import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryName;

import java.util.Set;

public interface CategoryRepositoryPort {

    Set<Category> findAllByIds(Set<CategoryId> categoryIds);

    Set<Category> findAllByNames(Set<CategoryName> categoryNames);

    Set<Category> findAll();

    Category create(Category category);

    Set<Category> createAll(Set<Category> categories);

}
