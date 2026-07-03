package com.dochiri.tooltestlab.category.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
        @Schema(description = "카테고리명", example = "전자기기")
        @NotBlank(message = "카테고리명은 필수입니다.")
        String name) {
}
