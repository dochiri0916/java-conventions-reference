package com.dochiri.tooltestlab.catalog.product.application.port.out;

import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import com.dochiri.tooltestlab.global.application.exception.RepositoryOperationException;

import java.util.Optional;

/**
 * 기술적 저장소 작업 실패는 {@link RepositoryOperationException}으로 전달한다.
 */
public interface ProductRepositoryPort {

    Product create(Product product);

    Product update(Product product);

    Optional<Product> findById(ProductId productId);

    Optional<Product> findByIdForUpdate(ProductId productId);

    ProductPage findAll(int page, int size);

}
