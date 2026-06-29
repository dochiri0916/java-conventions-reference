package com.dochiri.tooltestlab.category.application.command;

public record RegisterCategoryCommand(String name) {
    public RegisterCategoryCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("카테고리명은 빈 값일 수 없습니다.");
        }
    }
}
