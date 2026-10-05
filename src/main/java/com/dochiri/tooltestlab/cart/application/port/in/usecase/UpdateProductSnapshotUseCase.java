package com.dochiri.tooltestlab.cart.application.port.in.usecase;

import com.dochiri.tooltestlab.cart.application.port.in.command.UpdateProductSnapshotCommand;

@FunctionalInterface
public interface UpdateProductSnapshotUseCase {
    void update(UpdateProductSnapshotCommand command);
}
