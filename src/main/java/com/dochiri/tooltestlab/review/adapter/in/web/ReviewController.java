package com.dochiri.tooltestlab.review.adapter.in.web;

import com.dochiri.tooltestlab.global.web.LoginMember;
import com.dochiri.tooltestlab.review.adapter.in.web.request.WriteReviewRequest;
import com.dochiri.tooltestlab.review.adapter.in.web.response.ReviewResponse;
import com.dochiri.tooltestlab.review.application.command.WriteReviewCommand;
import com.dochiri.tooltestlab.review.application.port.in.DeleteReviewUseCase;
import com.dochiri.tooltestlab.review.application.port.in.GetProductReviewsUseCase;
import com.dochiri.tooltestlab.review.application.port.in.WriteReviewUseCase;
import com.dochiri.tooltestlab.review.application.result.GetProductReviewsResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "리뷰", description = "리뷰 작성, 조회, 삭제 API")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final WriteReviewUseCase writeReviewUseCase;
    private final GetProductReviewsUseCase getProductReviewsUseCase;
    private final DeleteReviewUseCase deleteReviewUseCase;

    public ReviewController(WriteReviewUseCase writeReviewUseCase,
                            GetProductReviewsUseCase getProductReviewsUseCase,
                            DeleteReviewUseCase deleteReviewUseCase) {
        this.writeReviewUseCase = writeReviewUseCase;
        this.getProductReviewsUseCase = getProductReviewsUseCase;
        this.deleteReviewUseCase = deleteReviewUseCase;
    }

    @Operation(summary = "리뷰 작성", description = "상품에 대한 리뷰를 작성합니다.")
    @PostMapping
    public ResponseEntity<Map<String, String>> write(@LoginMember String memberPublicId, @RequestBody WriteReviewRequest request) {
        var result = writeReviewUseCase.write(
                new WriteReviewCommand(memberPublicId, request.productPublicId(), request.rating(), request.content()));
        return ResponseEntity.ok(Map.of("publicId", result.publicId(), "message", "리뷰가 등록되었습니다."));
    }

    @Operation(summary = "상품 리뷰 조회", description = "특정 상품의 리뷰 목록을 조회합니다.")
    @GetMapping("/product/{productPublicId}")
    public ResponseEntity<ReviewResponse> getByProduct(
            @Parameter(description = "상품 publicId", example = "p1r2o3d4u5c6")
            @PathVariable String productPublicId) {
        GetProductReviewsResult result = getProductReviewsUseCase.getByProduct(productPublicId);
        var data = result.reviews().stream()
                .map(r -> new ReviewResponse.ReviewData(r.publicId(), r.memberPublicId(), r.rating(), r.content()))
                .toList();
        return ResponseEntity.ok(new ReviewResponse(result.productPublicId(), data, result.averageRating()));
    }

    @Operation(summary = "리뷰 삭제", description = "작성한 리뷰를 삭제합니다.")
    @DeleteMapping("/{publicId}")
    public ResponseEntity<Map<String, String>> delete(
            @LoginMember String memberPublicId,
            @Parameter(description = "리뷰 publicId", example = "r1e2v3i4e5w6")
            @PathVariable String publicId) {
        deleteReviewUseCase.delete(publicId, memberPublicId);
        return ResponseEntity.ok(Map.of("message", "리뷰가 삭제되었습니다."));
    }
}
