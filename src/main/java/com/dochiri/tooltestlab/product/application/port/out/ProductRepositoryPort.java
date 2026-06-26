package com.dochiri.tooltestlab.product.application.port.out;

import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.product.domain.model.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepositoryPort {
    Product save(Product product, List<Category> categories);

    Optional<Product> findByPublicId(String publicId);

    Page<Product> findAll(int page, int size);

    List<Category> findCategoriesByProductPublicId(String productPublicId);

    Category saveCategory(Category category);

    Optional<Category> findCategoryByPublicId(String publicId);

    List<Category> findCategoriesByPublicIds(Set<String> publicIds);

    List<Category> findAllCategories();
}
