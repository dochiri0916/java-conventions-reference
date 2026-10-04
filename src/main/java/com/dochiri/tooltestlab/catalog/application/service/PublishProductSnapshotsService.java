package com.dochiri.tooltestlab.catalog.application.service;

import com.dochiri.tooltestlab.catalog.application.CatalogEventMapper;
import com.dochiri.tooltestlab.catalog.application.port.in.PublishProductSnapshotsUseCase;
import com.dochiri.tooltestlab.catalog.application.port.out.IntegrationEventPort;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public final class PublishProductSnapshotsService implements PublishProductSnapshotsUseCase {
    private final ProductRepositoryPort productRepositoryPort;
    private final IntegrationEventPort integrationEventPort;

    @Override
    @Transactional
    public int publishPage(final int page, final int size) {
        final var snapshots = productRepositoryPort.findAll(page, size).products();
        snapshots.stream().sorted(Comparator.comparing(snapshot -> snapshot.id().value())).forEach(snapshot ->
                productRepositoryPort.findByIdForUpdate(snapshot.id()).ifPresent(current ->
                        integrationEventPort.publish(CatalogEventMapper.toIntegration(current.changedEvent()))));
        return snapshots.size();
    }
}
