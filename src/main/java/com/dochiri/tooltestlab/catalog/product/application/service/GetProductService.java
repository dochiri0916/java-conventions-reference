package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.product.application.exception.ProductApplicationException;
import com.dochiri.tooltestlab.catalog.product.application.port.in.GetProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.GetProductResult;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public final class GetProductService implements GetProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryReferenceQueryPort categoryReferenceQueryPort;

    @Override
    @Transactional(readOnly = true)
    public GetProductResult getProduct(final String publicId) {
        final Product product = productRepositoryPort.findById(publicId)
                .orElseThrow(() -> ProductApplicationException.notFound(publicId));

        final List<String> categoryNames = findCategoryNames(product.categoryReferences());

        return new GetProductResult(
                product.id().value(),
                product.name().value(),
                product.description().value(),
                product.price().amount(),
                product.stock().value(),
                product.status(),
                categoryNames
        );
    }

    private List<String> findCategoryNames(final Set<CategoryReferenceId> categoryReferences) {
        if (categoryReferences.isEmpty()) {
            return List.of();
        }

        return categoryReferenceQueryPort
                .findByIds(categoryReferences)
                .stream()
                .map(CategoryReferenceData::categoryName)
                .sorted()
                .toList();
    }

}
