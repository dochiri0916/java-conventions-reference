package com.dochiri.tooltestlab.product.application.service;

import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.product.application.port.in.GetProductResult;
import com.dochiri.tooltestlab.product.application.port.in.GetProductUseCase;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.domain.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public final class GetProductService implements GetProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public GetProductService(final ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public GetProductResult getProduct(final String publicId) {
        final Product product = productRepositoryPort.findByPublicId(publicId)
                .orElseThrow(() -> ProductNotFoundException.productNotFound(publicId));

        final List<Category> categories = productRepositoryPort.findCategoriesByProductPublicId(publicId);
        final List<String> categoryNames = categories.stream().map(c -> c.name().value()).toList();

        return new GetProductResult(
                product.id().value(), product.name().value(), product.description().value(),
                product.price().amount(), product.stock().value(),
                product.status().name(), categoryNames);
    }
}
