package com.dochiri.tooltestlab.cart.application.port.in.usecase;

@FunctionalInterface
public interface PublishCartSnapshotsUseCase {
    int publishPage(int page, int size);
}
