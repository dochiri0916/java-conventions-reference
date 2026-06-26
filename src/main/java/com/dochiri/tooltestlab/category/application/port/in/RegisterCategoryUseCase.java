package com.dochiri.tooltestlab.category.application.port.in;

import com.dochiri.tooltestlab.category.application.command.RegisterCategoryCommand;
import com.dochiri.tooltestlab.category.application.result.RegisterCategoryResult;

public interface RegisterCategoryUseCase {
    RegisterCategoryResult register(RegisterCategoryCommand command);
}
