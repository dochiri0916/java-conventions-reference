package com.dochiri.tooltestlab.product.application.service;

import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.product.application.port.in.ListProductsUseCase;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.application.result.ListProductsResult;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ListProductsService implements ListProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public ListProductsService(final ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public ListProductsResult list(final int page, final int size) {
        final Page<com.dochiri.tooltestlab.product.domain.model.Product> productPage =
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
