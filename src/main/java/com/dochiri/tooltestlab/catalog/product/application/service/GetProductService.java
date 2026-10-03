package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.category.domain.model.CategoryId;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.catalog.product.application.port.in.GetProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.GetProductResult;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public final class GetProductService implements GetProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetProductResult getProduct(final String publicId) {
        final Product product = productRepositoryPort.findById(new ProductId(publicId))
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

    private List<String> findCategoryNames(final Set<CategoryId> categoryIds) {
        if (categoryIds.isEmpty()) {
            return List.of();
        }

        return categoryRepositoryPort
                .findAllByIds(categoryIds)
                .stream()
                .map(category -> category.name().value())
                .sorted()
                .toList();
    }

}
