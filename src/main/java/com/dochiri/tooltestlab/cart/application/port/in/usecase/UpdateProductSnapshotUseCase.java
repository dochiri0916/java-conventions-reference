package com.dochiri.tooltestlab.cart.application.port.in.usecase;

import com.dochiri.tooltestlab.cart.application.port.in.command.UpdateProductSnapshotCommand;

public interface UpdateProductSnapshotUseCase {
    void update(UpdateProductSnapshotCommand command);
}
