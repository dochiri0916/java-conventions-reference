package com.dochiri.tooltestlab.cart.adapter.out.product;

import com.dochiri.tooltestlab.cart.application.port.out.ProductCatalogPort;
import com.dochiri.tooltestlab.cart.domain.model.ProductName;
import com.dochiri.tooltestlab.cart.domain.model.ProductReferenceId;
import com.dochiri.tooltestlab.catalog.product.application.port.in.GetProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.GetProductResult;
import com.dochiri.tooltestlab.global.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class ProductCatalogAdapter implements ProductCatalogPort {

    private final GetProductUseCase getProductUseCase;

    @Override
    public ProductSnapshot findProduct(final ProductReferenceId productId) {
        final GetProductResult product = getProductUseCase.getProduct(productId.value());
        return new ProductSnapshot(
                new ProductReferenceId(product.publicId()),
                new ProductName(product.name()),
                new Money(product.priceAmount()));
    }
}
