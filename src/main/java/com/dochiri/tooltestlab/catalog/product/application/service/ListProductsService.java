package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.Category;
import com.dochiri.tooltestlab.catalog.category.domain.vo.CategoryId;
import com.dochiri.tooltestlab.catalog.product.application.ProductResultMapper;
import com.dochiri.tooltestlab.catalog.product.application.port.in.query.ListProductsQuery;
import com.dochiri.tooltestlab.catalog.product.application.port.in.usecase.ListProductsUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.ListProductsResult;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductPage;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductSearchCondition;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductStatus;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public final class ListProductsService implements ListProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public ListProductsResult list(final ListProductsQuery query) {
        final ProductPage productPage = productRepositoryPort.search(toCondition(query), query.page(), query.size());
        final Map<CategoryId, Category> categoriesById = findCategoriesById(productPage.products());

        final List<ListProductsResult.ProductSummary> summaries = productPage.products().stream()
                .map(product -> toSummary(product, categoriesById))
                .toList();

        return new ListProductsResult(summaries, query.page(), query.size(), productPage.totalCount());
    }

    private static ProductSearchCondition toCondition(final ListProductsQuery query) {
        return new ProductSearchCondition(
                query.keyword(),
                query.status() == null ? null : ProductStatus.valueOf(query.status()),
                query.categoryId() == null ? null : CategoryId.parse(query.categoryId()),
                query.minPrice() == null ? null : Money.of(query.minPrice()),
                query.maxPrice() == null ? null : Money.of(query.maxPrice())
        );
    }

    private Map<CategoryId, Category> findCategoriesById(final List<Product> products) {
        final Set<CategoryId> categoryIds = products.stream()
                .flatMap(product -> product.categoryIds().values().stream())
                .collect(Collectors.toUnmodifiableSet());
        return categoryRepositoryPort.findAllByIds(categoryIds).stream()
                .collect(Collectors.toUnmodifiableMap(Category::id, Function.identity()));
    }

    private static ListProductsResult.ProductSummary toSummary(
            final Product product,
            final Map<CategoryId, Category> categoriesById
    ) {
        final List<Category> categories = product.categoryIds().values().stream()
                .map(categoriesById::get)
                .filter(Objects::nonNull)
                .toList();
        return ProductResultMapper.toSummary(product, categories);
    }
}
