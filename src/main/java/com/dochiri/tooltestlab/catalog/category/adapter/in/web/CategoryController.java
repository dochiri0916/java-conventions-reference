package com.dochiri.tooltestlab.catalog.category.adapter.in.web;

import com.dochiri.tooltestlab.catalog.category.adapter.in.web.request.CreateCategoryRequest;
import com.dochiri.tooltestlab.catalog.category.adapter.in.web.response.CategoryListResponse;
import com.dochiri.tooltestlab.catalog.category.adapter.in.web.response.CreateCategoryResponse;
import com.dochiri.tooltestlab.catalog.category.application.port.in.GetCategorySummariesUseCase;
import com.dochiri.tooltestlab.catalog.category.application.port.in.RegisterCategoryUseCase;
import com.dochiri.tooltestlab.catalog.category.application.port.in.command.RegisterCategoryCommand;
import com.dochiri.tooltestlab.catalog.category.application.port.in.result.CategorySummary;
import com.dochiri.tooltestlab.catalog.category.application.port.in.result.RegisterCategoryResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "카테고리", description = "카테고리 등록, 조회 API")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public final class CategoryController {

    private final RegisterCategoryUseCase registerCategoryUseCase;
    private final GetCategorySummariesUseCase getCategorySummariesUseCase;

    @Operation(summary = "카테고리 등록", description = "새로운 카테고리를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 카테고리명")
    })
    @PostMapping
    public ResponseEntity<CreateCategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        final RegisterCategoryResult result =
                registerCategoryUseCase.register(new RegisterCategoryCommand(request.name()));
        return ResponseEntity.ok(new CreateCategoryResponse(result.publicId(), result.name()));
    }

    @Operation(summary = "카테고리 목록 조회", description = "전체 카테고리 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<CategoryListResponse>> list() {
        final List<CategoryListResponse> response = getCategorySummariesUseCase.getAll().stream()
                .map(CategoryController::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    private static CategoryListResponse toResponse(final CategorySummary summary) {
        return new CategoryListResponse(summary.publicId(), summary.name());
    }
}
