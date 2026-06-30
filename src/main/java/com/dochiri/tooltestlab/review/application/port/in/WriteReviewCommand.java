package com.dochiri.tooltestlab.review.application.port.in;

public record WriteReviewCommand(String memberPublicId, String productPublicId, int rating, String content) {
    public WriteReviewCommand {
        if (memberPublicId == null || memberPublicId.isBlank()) {
            throw new IllegalArgumentException("회원 ID는 빈 값일 수 없습니다.");
        }
        if (productPublicId == null || productPublicId.isBlank()) {
            throw new IllegalArgumentException("상품 ID는 빈 값일 수 없습니다.");
        }
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("평점은 1~5 사이여야 합니다. 입력값: " + rating);
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("리뷰 내용은 빈 값일 수 없습니다.");
        }
    }
}
