package com.dochiri.tooltestlab.order.application.service;

import com.dochiri.tooltestlab.global.domain.vo.Money;
import com.dochiri.tooltestlab.global.domain.vo.Quantity;
import com.dochiri.tooltestlab.order.application.port.in.command.UpdateCartSnapshotCommand;
import com.dochiri.tooltestlab.order.application.port.in.usecase.UpdateCartSnapshotUseCase;
import com.dochiri.tooltestlab.order.application.port.out.CartSnapshotPort;
import com.dochiri.tooltestlab.order.domain.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class UpdateCartSnapshotService implements UpdateCartSnapshotUseCase {
    private final CartSnapshotPort cartSnapshotPort;

    @Override
    @Transactional
    public void update(final UpdateCartSnapshotCommand command) {
        cartSnapshotPort.update(new CartSnapshotPort.CartSnapshot(
                CartReferenceId.parse(command.cartId()), MemberReferenceId.parse(command.memberId()), command.revision(),
                command.items().stream().map(item -> OrderLine.of(
                        ProductReferenceId.parse(item.productId()), ProductName.of(item.productName()),
                        Quantity.of(item.quantity()), Money.of(item.unitPrice()))).toList()), command.sequence());
    }
}
