package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductStockOverflowException;
import com.dochiri.tooltestlab.catalog.product.application.port.in.IncreaseProductStockUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.exception.StockOverflowException;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.application.CatalogEventMapper;
import com.dochiri.tooltestlab.catalog.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class IncreaseProductStockService implements IncreaseProductStockUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public void increase(final String productPublicId, final int quantity) {
        final Product product = productRepositoryPort.findByIdForUpdate(ProductId.parse(productPublicId))
                .orElseThrow(ProductNotFoundException::new);
        try {
            final Product changed = productRepositoryPort.update(product.increaseStock(quantity));
            integrationEventPort.publish(CatalogEventMapper.toIntegration(changed.changedEvent()));
        } catch (final StockOverflowException exception) {
            throw new ProductStockOverflowException();
        }
    }
}
