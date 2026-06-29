package com.dochiri.tooltestlab.product.application.service;

import com.dochiri.tooltestlab.category.domain.model.Category;
import com.dochiri.tooltestlab.product.application.port.in.GetProductUseCase;
import com.dochiri.tooltestlab.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.product.application.result.GetProductResult;
import com.dochiri.tooltestlab.product.domain.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetProductService implements GetProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public GetProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public GetProductResult getProduct(String publicId) {
        Product product = productRepositoryPort.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. publicId=" + publicId));

        List<Category> categories = productRepositoryPort.findCategoriesByProductPublicId(publicId);
        List<String> categoryNames = categories.stream().map(Category::name).toList();

        return new GetProductResult(
                product.id().value(), product.name().value(), product.description().value(),
                product.price().amount(), product.stock().value(),
                product.status().name(), categoryNames);
    }
}
