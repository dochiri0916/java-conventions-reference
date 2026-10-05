package com.dochiri.tooltestlab.catalog.category.adapter.in.web.request;

import com.dochiri.tooltestlab.catalog.category.application.port.in.command.RegisterCategoryCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
        @Schema(description = "카테고리명", example = "전자기기")
        @NotBlank(message = "카테고리명은 필수입니다.")
        String name) {

    public RegisterCategoryCommand toCommand() {
        return new RegisterCategoryCommand(name);
    }
}
