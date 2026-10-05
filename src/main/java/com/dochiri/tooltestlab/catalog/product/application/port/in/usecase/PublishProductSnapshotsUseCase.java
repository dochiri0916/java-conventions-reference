package com.dochiri.tooltestlab.catalog.product.application.port.in.usecase;

@FunctionalInterface
public interface PublishProductSnapshotsUseCase {
    int publishPage(int page, int size);
}
