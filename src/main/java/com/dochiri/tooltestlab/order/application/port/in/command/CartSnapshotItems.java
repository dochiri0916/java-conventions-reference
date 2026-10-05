package com.dochiri.tooltestlab.order.application.port.in.command;

import java.util.List;

public record CartSnapshotItems(List<UpdateCartSnapshotCommand.Item> values) {

    public CartSnapshotItems {
        values = List.copyOf(values);
    }

}
