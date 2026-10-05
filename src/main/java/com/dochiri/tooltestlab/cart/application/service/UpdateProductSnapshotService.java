package com.dochiri.tooltestlab.cart.application.service;

import com.dochiri.tooltestlab.cart.application.port.in.command.UpdateProductSnapshotCommand;
import com.dochiri.tooltestlab.cart.application.port.in.usecase.UpdateProductSnapshotUseCase;
import com.dochiri.tooltestlab.cart.application.port.out.ProductCatalogPort;
import com.dochiri.tooltestlab.cart.domain.vo.ProductName;
import com.dochiri.tooltestlab.cart.domain.vo.ProductReferenceId;
import com.dochiri.tooltestlab.global.domain.vo.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public final class UpdateProductSnapshotService implements UpdateProductSnapshotUseCase {
    private final ProductCatalogPort productCatalogPort;

    @Override
    @Transactional
    public void update(final UpdateProductSnapshotCommand command) {
        productCatalogPort.save(new ProductCatalogPort.ProductSnapshot(
                ProductReferenceId.parse(command.productId()), ProductName.of(command.name()),
                Money.of(command.priceAmount())), command.sequence());
    }
}
