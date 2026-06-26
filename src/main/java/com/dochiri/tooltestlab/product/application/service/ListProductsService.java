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

    public ListProductsService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public ListProductsResult list(int page, int size) {
        Page<com.dochiri.tooltestlab.product.domain.model.Product> productPage =
                productRepositoryPort.findAll(page, size);

        List<ListProductsResult.ProductSummary> summaries = productPage.getContent().stream()
                .map(product -> {
                    List<Category> categories = productRepositoryPort.findCategoriesByProductPublicId(product.publicId());
                    List<String> categoryNames = categories.stream().map(Category::name).toList();
                    return new ListProductsResult.ProductSummary(
                            product.publicId(), product.name(), product.price().amount(),
                            product.stock(), product.status().name(), categoryNames);
                })
                .toList();

        return new ListProductsResult(summaries, page, size, productPage.getTotalElements());
    }
}
