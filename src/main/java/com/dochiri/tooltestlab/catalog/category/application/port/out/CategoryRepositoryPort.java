package com.dochiri.tooltestlab.catalog.category.application.port.out;

import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryRepositoryPort {

    Category save(Category category);

    Optional<Category> findByPublicId(String publicId);

    List<Category> findAllByPublicIds(Set<String> publicIds);

    Set<Category> findAllByIds(Set<CategoryId> categoryIds);

}
