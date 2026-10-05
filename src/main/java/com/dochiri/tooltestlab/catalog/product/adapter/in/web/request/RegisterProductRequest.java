package com.dochiri.tooltestlab.catalog.product.adapter.in.web.request;

import com.dochiri.tooltestlab.catalog.product.application.port.in.command.RegisterProductCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "상품 등록 요청")
public record RegisterProductRequest(
        @Schema(description = "상품명", example = "맥북 프로 16인치")
        @NotBlank(message = "상품명은 필수입니다.")
        String name,
        @Schema(description = "상품 설명", example = "Apple M4 Pro 칩 탑재")
        @NotBlank(message = "상품 설명은 필수입니다.")
        String description,
        @Schema(description = "가격 (원 단위)", example = "3690000")
        @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
        int priceAmount,
        @Schema(description = "재고 수량", example = "100")
        @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
        int stock,
        @Schema(description = "카테고리 ID 목록", example = "[\"cat01\", \"cat02\"]")
        @NotEmpty(message = "카테고리는 하나 이상 필수입니다.")
        List<String> categoryIds) {
    public RegisterProductRequest {
        categoryIds = categoryIds != null ? List.copyOf(categoryIds) : List.of();
    }

    public RegisterProductCommand toCommand() {
        return new RegisterProductCommand(name, description, priceAmount, stock,
                categoryIds.stream().collect(Collectors.toUnmodifiableSet()));
    }
}
