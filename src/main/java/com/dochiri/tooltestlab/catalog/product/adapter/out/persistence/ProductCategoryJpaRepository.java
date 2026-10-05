package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

interface ProductCategoryJpaRepository extends JpaRepository<ProductCategoryEntity, Long> {

    List<ProductCategoryEntity> findByProductId(String productId);

    List<ProductCategoryEntity> findByProductIdIn(Collection<String> productIds);

}
