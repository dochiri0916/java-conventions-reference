package com.dochiri.tooltestlab.review.adapter.in.web;

import com.dochiri.tooltestlab.review.adapter.in.web.request.WriteReviewRequest;
import com.dochiri.tooltestlab.review.adapter.in.web.response.DeleteReviewResponse;
import com.dochiri.tooltestlab.review.adapter.in.web.response.GetProductReviewsResponse;
import com.dochiri.tooltestlab.review.adapter.in.web.response.WriteReviewResponse;
import com.dochiri.tooltestlab.review.application.port.in.query.GetProductReviewsQuery;
import com.dochiri.tooltestlab.review.application.port.in.usecase.DeleteReviewUseCase;
import com.dochiri.tooltestlab.review.application.port.in.result.GetProductReviewsResult;
import com.dochiri.tooltestlab.review.application.port.in.usecase.GetProductReviewsUseCase;
import com.dochiri.tooltestlab.review.application.port.in.usecase.WriteReviewUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "리뷰", description = "리뷰 작성, 조회, 삭제 API")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final WriteReviewUseCase writeReviewUseCase;
    private final GetProductReviewsUseCase getProductReviewsUseCase;
    private final DeleteReviewUseCase deleteReviewUseCase;

    @Operation(summary = "리뷰 작성", description = "상품에 대한 리뷰를 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 평점 또는 빈 리뷰 내용"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "404", description = "상품이 존재하지 않음"),
            @ApiResponse(responseCode = "409", description = "이미 리뷰를 작성한 상품")
    })
    @PostMapping
    public ResponseEntity<WriteReviewResponse> write(
            @Parameter(hidden = true) String memberPublicId,
            @Valid @RequestBody WriteReviewRequest request) {
        final var result = writeReviewUseCase.write(
                request.toCommand(memberPublicId));
        return ResponseEntity.ok(WriteReviewResponse.from(result));
    }

    @Operation(summary = "상품 리뷰 조회", description = "특정 상품의 리뷰 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 목록 조회 성공")
    })
    @GetMapping("/product/{productPublicId}")
    public ResponseEntity<GetProductReviewsResponse> getByProduct(
            @Parameter(description = "상품 publicId", example = "01900000-0000-7000-8000-000000000001")
            @PathVariable String productPublicId) {
        final GetProductReviewsResult result =
                getProductReviewsUseCase.getByProduct(new GetProductReviewsQuery(productPublicId));
        return ResponseEntity.ok(GetProductReviewsResponse.from(result));
    }

    @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
            @ApiResponse(responseCode = "403", description = "리뷰 작성자가 아님"),
            @ApiResponse(responseCode = "404", description = "리뷰가 존재하지 않음")
    })
    @DeleteMapping("/{publicId}")
    public ResponseEntity<DeleteReviewResponse> delete(
            @Parameter(hidden = true) String memberPublicId,
            @Parameter(description = "리뷰 publicId", example = "01900000-0000-7000-8000-000000000001")
            @PathVariable String publicId) {
        deleteReviewUseCase.delete(publicId, memberPublicId);
        return ResponseEntity.ok(new DeleteReviewResponse("리뷰가 삭제되었습니다."));
    }
}
