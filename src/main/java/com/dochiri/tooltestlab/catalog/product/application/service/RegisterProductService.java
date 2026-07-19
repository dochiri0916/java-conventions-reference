package com.dochiri.tooltestlab.catalog.product.application.service;

import com.dochiri.tooltestlab.catalog.product.application.port.in.RegisterProductUseCase;
import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import com.dochiri.tooltestlab.catalog.product.application.port.out.ProductRepositoryPort;
import com.dochiri.tooltestlab.catalog.product.domain.model.Product;
import com.dochiri.tooltestlab.catalog.product.domain.model.ProductId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public final class RegisterProductService implements RegisterProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    @Override
    @Transactional
    public ProductId register(final RegisterProductCommand command) {

        return null;
    }

}
