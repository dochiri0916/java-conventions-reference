package com.dochiri.tooltestlab.catalog.category.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findAllByPublicIdIn(Collection<String> publicIds);

}
