package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
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
    public Product save(final Product product) {
        final ProductEntity entity = productJpaRepository
                .findByPublicId(product.id().value())
                .map(existing -> update(existing, product))
                .orElseGet(() -> ProductMapper.toEntity(product));

        final ProductEntity saved = productJpaRepository.save(entity);

        return ProductMapper.toDomain(saved);
    }

    @Override
    public Optional<Product> findByPublicId(final String publicId) {
        return productJpaRepository.findByPublicId(publicId).map(ProductMapper::toDomain);
    }

    @Override
    public Page<Product> findAll(final int page, final int size) {
        return productJpaRepository.findAll(PageRequest.of(page, size))
                .map(ProductMapper::toDomain);
    }

    private static ProductEntity update(final ProductEntity entity, final Product product) {
        ProductMapper.updateEntity(entity, product);
        return entity;
    }

}
