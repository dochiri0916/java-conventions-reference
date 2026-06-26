package com.dochiri.tooltestlab.review.domain.model;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

/**
 * 상품 리뷰 Entity.
 */
public record Review(
        String publicId,
        String memberPublicId,
        String productPublicId,
        int rating,
        String content
) {

    public Review {
        requireNonNull(publicId, "publicId는 null일 수 없습니다.");
        requireNonNull(memberPublicId, "회원 ID는 null일 수 없습니다.");
        requireNonNull(productPublicId, "상품 ID는 null일 수 없습니다.");
        requireNonNull(content, "리뷰 내용은 null일 수 없습니다.");

        content = content.strip();

        if (publicId.isBlank()) throw new IllegalArgumentException("publicId는 빈 값일 수 없습니다.");
        if (memberPublicId.isBlank()) throw new IllegalArgumentException("회원 ID는 빈 값일 수 없습니다.");
        if (productPublicId.isBlank()) throw new IllegalArgumentException("상품 ID는 빈 값일 수 없습니다.");
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("평점은 1~5 사이여야 합니다. 입력값: " + rating);
        if (content.isBlank()) throw new IllegalArgumentException("리뷰 내용은 빈 값일 수 없습니다.");
    }

    public static Review create(String memberPublicId, String productPublicId, int rating, String content) {
        return new Review(
                UUID.randomUUID().toString().replace("-", ""),
                memberPublicId, productPublicId, rating, content);
    }
}
