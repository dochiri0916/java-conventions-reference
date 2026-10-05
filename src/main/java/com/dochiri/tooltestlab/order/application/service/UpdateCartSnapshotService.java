package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.order.application.port.in.command.CartSnapshotItems;
import com.dochiri.tooltestlab.order.application.port.in.command.UpdateCartSnapshotCommand;
import com.dochiri.tooltestlab.order.application.port.in.usecase.UpdateCartSnapshotUseCase;
import com.dochiri.tooltestlab.order.application.port.out.CartSnapshotLines;
import com.dochiri.tooltestlab.order.application.port.out.CartSnapshotPort;
import com.dochiri.tooltestlab.order.domain.vo.CartReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.MemberReferenceId;
import com.dochiri.tooltestlab.order.domain.vo.OrderLine;
import com.dochiri.tooltestlab.order.domain.vo.ProductName;
import com.dochiri.tooltestlab.order.domain.vo.ProductReferenceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class UpdateCartSnapshotService implements UpdateCartSnapshotUseCase {
    private final CartSnapshotPort cartSnapshotPort;

    @Override
    @Transactional
    public void update(final UpdateCartSnapshotCommand command) {
        final CartSnapshotItems commandItems = command.items();
        final List<OrderLine> lines = commandItems.values().stream()
                .map(UpdateCartSnapshotService::toOrderLine)
                .toList();
        final CartSnapshotPort.CartSnapshot snapshot = new
                CartSnapshotPort.CartSnapshot(
                CartReferenceId.parse(
                        command.cartId()),
                MemberReferenceId.parse(
                        command.memberId()),
                command.revision(),
                new CartSnapshotLines(lines));
        cartSnapshotPort.update(snapshot,
                command.sequence());
    }

    private static OrderLine toOrderLine(final UpdateCartSnapshotCommand.Item item) {
        return OrderLine.of(
                ProductReferenceId.parse(
                        item.productId()),
                ProductName.of(
                        item.productName()),
                Quantity.of(
                        item.quantity()),
                Money.of(
                        item.unitPrice()));
    }
}
