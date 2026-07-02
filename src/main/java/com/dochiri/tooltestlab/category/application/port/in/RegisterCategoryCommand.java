package com.dochiri.tooltestlab.category.application.port.in;

import com.dochiri.tooltestlab.category.application.exception.InvalidCommandException;

public record RegisterCategoryCommand(String name) {
    public RegisterCategoryCommand {
        if (name == null || name.isBlank()) {
            throw InvalidCommandException.invalidInput("카테고리명은 빈 값일 수 없습니다.");
        }
    }
}
