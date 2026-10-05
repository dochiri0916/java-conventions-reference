package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.catalog.product.application.port.in.query.GetProductQuery;
import com.dochiri.tooltestlab.catalog.product.application.port.in.usecase.GetProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.GetProductResult;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import com.dochiri.tooltestlab.catalog.product.domain.vo.CategoryIds;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class GetProductService implements GetProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetProductResult getProduct(final GetProductQuery query) {
        final Product product = productRepositoryPort.findById(ProductId.parse(query.productPublicId()))
                .orElseThrow(() -> new ProductNotFoundException());

        final List<String> categoryNames = findCategoryNames(product.categoryIds());

        return new GetProductResult(
                product.id().value(),
                product.name().value(),
                product.description().value(),
                product.price().amount(),
                product.stock().value(),
                product.status().name(),
                categoryNames
        );
    }

    private List<String> findCategoryNames(final CategoryIds categoryIds) {
        if (categoryIds.isEmpty()) {
            return List.of();
        }

        return categoryRepositoryPort
                .findAllByIds(categoryIds.values())
                .stream()
                .map(category -> category.name().value())
                .sorted()
                .toList();
    }

}
