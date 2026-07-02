package com.dochiri.tooltestlab.category.adapter.in.web;

import com.dochiri.tooltestlab.category.adapter.in.web.response.CategoryListResponse;
import com.dochiri.tooltestlab.category.application.port.in.RegisterCategoryCommand;
import com.dochiri.tooltestlab.category.application.port.in.RegisterCategoryResult;
import com.dochiri.tooltestlab.category.application.port.in.RegisterCategoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Tag(name = "카테고리", description = "카테고리 등록, 조회 API")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final RegisterCategoryUseCase registerCategoryUseCase;

    @Operation(summary = "카테고리 등록", description = "새로운 카테고리를 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 등록 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 카테고리명")
    })
    @PostMapping
    public ResponseEntity<Map<String, String>> create(
            @RequestBody(description = "카테고리 정보",
                    content = @Content(schema = @Schema(implementation = CreateCategoryBody.class)))
            @org.springframework.web.bind.annotation.RequestBody Map<String, String> body) {
        final RegisterCategoryResult result = registerCategoryUseCase.register(new RegisterCategoryCommand(body.get("name")));
        return ResponseEntity.ok(Map.of("publicId", result.publicId(), "name", result.name()));
    }

    @Operation(summary = "카테고리 목록 조회", description = "전체 카테고리 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<CategoryListResponse>> list() {
        return ResponseEntity.ok(List.of());
    }

    @Schema(description = "카테고리 생성 요청")
    record CreateCategoryBody(
            @Schema(description = "카테고리명", example = "전자기기") String name) {
    }
}
