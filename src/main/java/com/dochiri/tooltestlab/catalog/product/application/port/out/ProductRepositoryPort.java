package com.dochiri.tooltestlab.catalog.product.application.port.out;

import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findByPublicId(String publicId);

    Page<Product> findAll(int page, int size);

}
