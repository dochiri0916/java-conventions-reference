package com.dochiri.tooltestlab.category.application.port.in;

@FunctionalInterface
public interface RegisterCategoryUseCase {
    RegisterCategoryResult register(RegisterCategoryCommand command);
}
