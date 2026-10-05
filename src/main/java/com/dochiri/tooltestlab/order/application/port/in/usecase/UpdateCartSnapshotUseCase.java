package com.dochiri.tooltestlab.order.application.port.in.usecase;

import com.dochiri.tooltestlab.order.application.port.in.command.UpdateCartSnapshotCommand;

public interface UpdateCartSnapshotUseCase {
    void update(UpdateCartSnapshotCommand command);
}
