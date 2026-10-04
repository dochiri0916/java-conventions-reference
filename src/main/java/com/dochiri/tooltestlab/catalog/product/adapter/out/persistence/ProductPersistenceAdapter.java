package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductPage;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;

@Repository
@RequiredArgsConstructor
class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;
    private final EntityManager entityManager;

    @Override
    public Product create(final Product product) {
        return execute(() -> {
            final ProductEntity entity = ProductMapper.toEntity(product);

            final ProductEntity saved = productJpaRepository.save(entity);

            return ProductMapper.toDomain(saved);
        });
    }

    @Override
    public Product update(final Product product) {
        return execute(() -> {
            final ProductEntity entity = productJpaRepository.findByPublicId(product.id().value())
                    .orElseThrow(() -> new IllegalArgumentException(product.id().value()));

            ProductMapper.updateEntity(entity, product);

            return ProductMapper.toDomain(entity);
        });
    }

    @Override
    public Optional<Product> findById(final ProductId productId) {
        return execute(() -> productJpaRepository
                .findByPublicId(productId.value())
                .map(ProductMapper::toDomain));
    }

    @Override
    public Optional<Product> findByIdForUpdate(final ProductId productId) {
        return execute(() -> productJpaRepository
                .findWithLockByPublicId(productId.value())
                .map(entity -> {
                    entityManager.refresh(entity);
                    return ProductMapper.toDomain(entity);
                }));
    }

    @Override
    public ProductPage findAll(final int page, final int size) {
        return execute(() -> {
            final Page<ProductEntity> productPage =
                    productJpaRepository.findAll(PageRequest.of(page, size));

            return new ProductPage(
                    productPage.getContent().stream().map(ProductMapper::toDomain).toList(),
                    productPage.getTotalElements()
            );
        });
    }

}
