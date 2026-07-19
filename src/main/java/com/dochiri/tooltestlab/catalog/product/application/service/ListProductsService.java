package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.application.port.in.ListProductsUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.ListProductsResult;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductPage;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class ListProductsService implements ListProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public ListProductsResult list(final int page, final int size) {
        final ProductPage productPage =
                productRepositoryPort.findAll(page, size);

        final List<ListProductsResult.ProductSummary> summaries = productPage.products().stream()
                .map(product -> {
                    final List<String> categoryNames = categoryRepositoryPort
                            .findAllByIds(product.categoryIds())
                            .stream()
                            .map(category -> category.name().value())
                            .sorted()
                            .toList();
                    return new ListProductsResult.ProductSummary(
                            product.id().value(), product.name().value(), product.price().amount(),
                            product.stock().value(), product.status(), categoryNames);
                })
                .toList();

        return new ListProductsResult(summaries, page, size, productPage.totalCount());
    }
}
