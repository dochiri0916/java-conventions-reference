package com.dochiri.tooltestlab.catalog.category.adapter.in.web.response;

import com.dochiri.tooltestlab.catalog.category.application.port.in.result.CategorySummary;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 목록 응답")
public record CategoryListResponse(
        @Schema(description = "카테고리 publicId", example = "01900000-0000-7000-8000-000000000001") String publicId,
        @Schema(description = "카테고리명", example = "전자기기") String name) {

    public static CategoryListResponse from(final CategorySummary result) {
        return new CategoryListResponse(result.publicId(), result.name());
    }
}
