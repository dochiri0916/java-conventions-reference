package com.dochiri.tooltestlab.cart.adapter.out.product;

import com.dochiri.tooltestlab.cart.application.port.out.ProductCatalogPort;
import com.dochiri.tooltestlab.cart.domain.vo.ProductName;
import com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.catalog.product.application.port.in.GetProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.result.GetProductResult;
import com.dochiri.tooltestlab.global.domain.vo.Money;
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
                ProductReferenceId.parse(product.publicId()),
                ProductName.of(product.name()),
                Money.of(product.priceAmount()));
    }
}
