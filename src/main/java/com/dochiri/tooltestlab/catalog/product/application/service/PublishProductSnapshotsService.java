package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.product.application.CatalogEventMapper;
import com.dochiri.tooltestlab.catalog.product.application.port.in.usecase.PublishProductSnapshotsUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductPage;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.application.port.out.Products;
import com.dochiri.tooltestlab.catalog.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public final class PublishProductSnapshotsService implements PublishProductSnapshotsUseCase {
    private final ProductRepositoryPort productRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public int publishPage(final int page, final int size) {
        final ProductPage found = productRepositoryPort.findAll(page, size);
        final Products products = found.products();
        final List<Product> snapshots = products.values();
        snapshots.stream().sorted(Comparator.comparing(snapshot -> snapshot.id().value())).forEach(snapshot ->
                productRepositoryPort.findByIdForUpdate(snapshot.id()).ifPresent(current ->
                        integrationEventPort.publish(CatalogEventMapper.toIntegration(current.changedEvent()))));
        return snapshots.size();
    }
}
