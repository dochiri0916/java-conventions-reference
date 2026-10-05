package com.dochiri.tooltestlab.catalog.category.adapter.in.web.response;

import com.dochiri.tooltestlab.catalog.category.application.port.in.result.RegisterCategoryResult;

public record CreateCategoryResponse(String publicId, String name) {

    public static CreateCategoryResponse from(final RegisterCategoryResult result) {
        return new CreateCategoryResponse(result.publicId(), result.name());
    }
}
