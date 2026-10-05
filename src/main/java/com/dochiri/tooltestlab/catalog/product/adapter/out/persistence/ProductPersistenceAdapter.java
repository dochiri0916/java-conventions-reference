package com.dochiri.tooltestlab.catalog.product.adapter.out.persistence;

import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductPage;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductSearchCondition;
import com.dochiri.tooltestlab.catalog.product.application.port.out.Products;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dochiri.tooltestlab.global.persistence.RepositoryExceptionTranslator.execute;
import static java.util.stream.Collectors.groupingBy;

@Repository
@RequiredArgsConstructor
final class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;
    private final ProductCategoryJpaRepository productCategoryJpaRepository;
    private final ProductQueryRepository productQueryRepository;
    private final EntityManager entityManager;

    @Override
    public Product create(final Product product) {
        return execute(() -> {
            ProductEntity entity = ProductMapper.toEntity(product);

            final ProductEntity saved = productJpaRepository.saveAndFlush(entity);
            final List<ProductCategoryEntity> categories =
                    productCategoryJpaRepository.saveAllAndFlush(
                            ProductMapper.toCategoryEntities(product));

            return ProductMapper.toDomain(saved, categories);
        });
    }

    @Override
    public Product update(final Product product) {
        return execute(() -> {
            ProductEntity entity = productJpaRepository.findByPublicId(
                    product.id()
                            .value())
                    .orElseThrow(() -> new IllegalArgumentException(
                            product.id()
                                    .value()));

            ProductMapper.updateEntity(entity, product);
            List<ProductCategoryEntity> categories = replaceCategories(product);

            return ProductMapper.toDomain(entity, categories);
        });
    }

    @Override
    public Optional<Product> findById(final ProductId productId) {
        return execute(() -> productJpaRepository
                .findByPublicId(
                        productId.value())
                .map(entity ->
                        ProductMapper.toDomain(
                        entity, productCategoryJpaRepository.findByProductId(
                                entity.getPublicId()))));
    }

    @Override
    public Optional<Product> findByIdForUpdate(final ProductId productId) {
        return execute(() -> productJpaRepository
                .findWithLockByPublicId(
                        productId.value())
                .map(entity -> {
                    entityManager.refresh(entity);
                    return ProductMapper.toDomain(
                            entity, productCategoryJpaRepository.findByProductId(
                                    entity.getPublicId()));
                }));
    }

    @Override
    public ProductPage findAll(final int page, final int size) {
        return execute(() -> {
            Page<ProductEntity> productPage =
                    productJpaRepository.findAll(
                            PageRequest.of(page, size));

            return toProductPage(
                    productPage.getContent().stream().map(ProductRow::from).toList(),
                    productPage.getTotalElements()
            );
        });
    }

    @Override
    public ProductPage search(final ProductSearchCondition condition, final int page, final int size) {
        return execute(() -> toProductPage(
                productQueryRepository.search(condition, page, size),
                productQueryRepository.count(condition)
        ));
    }

    private ProductPage toProductPage(final List<ProductRow> rows, final long totalCount) {
        final List<String> productIds = rows.stream()
                .map(ProductRow::publicId)
                .toList();
        final Map<String, List<ProductCategoryEntity>> categoriesByProductId =
                productCategoryJpaRepository.findByProductIdIn(productIds).stream()
                        .collect(groupingBy(ProductCategoryEntity::getProductId));

        final List<Product> products = rows.stream()
                .map(row -> ProductMapper.toDomain(
                        row, categoriesByProductId.getOrDefault(row.publicId(), List.of())))
                .toList();
        return new ProductPage(new Products(products), totalCount);
    }

    private List<ProductCategoryEntity> replaceCategories(final Product product) {
        final String productId = product.id()
                .value();
        final List<ProductCategoryEntity> current = productCategoryJpaRepository.findByProductId(productId);
        final Set<String> requested = product.categoryIds().values().stream()
                .map(CategoryId::value)
                .collect(Collectors.toUnmodifiableSet());
        final Set<String> existing = current.stream()
                .map(ProductCategoryEntity::getCategoryId)
                .collect(Collectors.toUnmodifiableSet());

        final List<ProductCategoryEntity> removed = current.stream()
                .filter(category -> !requested.contains(category.getCategoryId()))
                .toList();
        final List<ProductCategoryEntity> added = ProductMapper.toCategoryEntities(product).stream()
                .filter(category -> !existing.contains(category.getCategoryId()))
                .toList();

        productCategoryJpaRepository.deleteAll(removed);
        productCategoryJpaRepository.flush();
        productCategoryJpaRepository.saveAll(added);
        productCategoryJpaRepository.flush();

        return Stream.concat(
                current.stream().filter(category -> requested.contains(category.getCategoryId())),
                added.stream()
        ).toList();
    }

}
