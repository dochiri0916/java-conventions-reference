package com.dochiri.tooltestlab.catalog.category.application.port.in.usecase;

import com.dochiri.tooltestlab.catalog.category.application.port.in.command.RegisterCategoryCommand;
import com.dochiri.tooltestlab.catalog.category.application.port.in.result.RegisterCategoryResult;

@FunctionalInterface
public interface RegisterCategoryUseCase {
    RegisterCategoryResult register(RegisterCategoryCommand command);
}
