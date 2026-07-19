package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product create(final Product product) {
        final ProductEntity entity = ProductMapper.toEntity(product);

        final ProductEntity saved = productJpaRepository.save(entity);

        return ProductMapper.toDomain(saved);
    }

    @Override
    public Product update(final Product product) {
        final ProductEntity entity = productJpaRepository.findByPublicId(product.id().value())
                .orElseThrow(() -> new IllegalArgumentException(product.id().value()));

        ProductMapper.updateEntity(entity, product);

        return ProductMapper.toDomain(entity);
    }

    @Override
    public Optional<Product> findById(final ProductId productId) {
        return productJpaRepository
                .findByPublicId(productId.value())
                .map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findAll(final int page, final int size) {
        return productJpaRepository.findAll(PageRequest.of(page, size))
                .map(ProductMapper::toDomain);
    }

}
