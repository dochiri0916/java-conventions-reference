package com.dochiri.tooltestlab.catalog.product.application.port.out;

import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import java.util.Optional;

public interface ProductRepositoryPort {

    Product create(Product product);

    Product update(Product product);

    Optional<Product> findById(ProductId productId);

    ProductPage findAll(int page, int size);

}
