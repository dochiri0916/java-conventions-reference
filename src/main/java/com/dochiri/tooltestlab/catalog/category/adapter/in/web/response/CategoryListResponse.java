package com.dochiri.tooltestlab.catalog.category.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리 목록 응답")
public record CategoryListResponse(
        @Schema(description = "카테고리 publicId", example = "c1a2t3e4g5o6") String publicId,
        @Schema(description = "카테고리명", example = "전자기기") String name) {
}
