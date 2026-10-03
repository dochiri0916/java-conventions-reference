package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.product.application.exception.InsufficientProductStockException;
import com.dochiri.tooltestlab.catalog.product.application.exception.ProductNotFoundException;
import com.dochiri.tooltestlab.catalog.product.application.port.in.DecreaseProductStockUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.exception.InsufficientStockException;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import com.dochiri.tooltestlab.catalog.product.domain.vo.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class DecreaseProductStockService implements DecreaseProductStockUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    @Transactional
    public void decrease(final String productPublicId, final int quantity) {
        final Product product = productRepositoryPort.findByIdForUpdate(ProductId.parse(productPublicId))
                .orElseThrow(ProductNotFoundException::new);
        try {
            productRepositoryPort.update(product.decreaseStock(quantity));
        } catch (final InsufficientStockException exception) {
            throw new InsufficientProductStockException();
        }
    }
}
