package com.dochiri.tooltestlab.category.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByCategoryId(String categoryId);

    List<CategoryEntity> findAllByCategoryIdIn(List<String> categoryIds);
}
