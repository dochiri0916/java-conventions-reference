package com.dochiri.tooltestlab.catalog.product.application.port.out;

import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductRepositoryPort {

    Product create(Product product);

    Product update(Product product);

    Optional<Product> findById(ProductId productId);

    Page<Product> findAll(int page, int size);

}
