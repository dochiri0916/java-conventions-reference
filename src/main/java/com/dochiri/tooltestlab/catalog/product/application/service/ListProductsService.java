package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.category.domain.model.Category;
import com.dochiri.tooltestlab.catalog.product.application.port.in.ListProductsUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.ListProductsResult;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class ListProductsService implements ListProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public ListProductsResult list(final int page, final int size) {
        final Page<Product> productPage =
                productRepositoryPort.findAll(page, size);

        final List<ListProductsResult.ProductSummary> summaries = productPage.getContent().stream()
                .map(product -> {
                    final List<Category> categories =
                            productRepositoryPort.findCategoriesByProductPublicId(product.id().value());
                    final List<String> categoryNames = categories.stream().map(c -> c.name().value()).toList();
                    return new ListProductsResult.ProductSummary(
                            product.id().value(), product.name().value(), product.price().amount(),
                            product.stock().value(), product.status().name(), categoryNames);
                })
                .toList();

        return new ListProductsResult(summaries, page, size, productPage.getTotalElements());
    }
}
