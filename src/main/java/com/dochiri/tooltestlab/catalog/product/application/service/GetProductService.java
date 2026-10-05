package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.category.application.port.out.Categories;
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryIdSet;
import com.dochiri.tooltestlab.catalog.category.application.port.out.CategoryRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.application.ProductResultMapper;
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

import java.util.Set;

@Service
@RequiredArgsConstructor
public final class GetProductService implements GetProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final CategoryRepositoryPort categoryRepositoryPort;

    @Override
    @Transactional(readOnly = true)
    public GetProductResult getProduct(final GetProductQuery query) {
        final Product product = productRepositoryPort.findById(
                ProductId.parse(
                        query.productPublicId()))
                .orElseThrow(() -> new ProductNotFoundException());

        final Categories categories = findCategories(product.categoryIds());
        return ProductResultMapper.toGetResult(product, categories);
    }

    private Categories findCategories(final CategoryIds categoryIds) {
        if (categoryIds.isEmpty()) {
            return new Categories(Set.of());
        }
        return categoryRepositoryPort.findAllByIds(new CategoryIdSet(
                categoryIds.values()));
    }

}
