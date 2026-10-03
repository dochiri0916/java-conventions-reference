package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductStockOverflowException;
import com.dochiri.tooltestlab.catalog.product.application.port.in.IncreaseProductStockUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.exception.StockOverflowException;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class IncreaseProductStockService implements IncreaseProductStockUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    @Transactional
    public void increase(final String productPublicId, final int quantity) {
        final Product product = productRepositoryPort.findByIdForUpdate(new ProductId(productPublicId))
                .orElseThrow(ProductNotFoundException::new);
        try {
            productRepositoryPort.update(product.increaseStock(quantity));
        } catch (final StockOverflowException exception) {
            throw new ProductStockOverflowException();
        }
    }
}
